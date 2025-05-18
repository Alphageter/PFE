package com.gestionpfes.adnan.Controllers.gestionbooking;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;
import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import com.gestionpfes.adnan.models.Booking;
import com.gestionpfes.adnan.models.Etudiant;
import com.gestionpfes.adnan.models.Groupe;
import com.gestionpfes.adnan.services.BookingService;
import com.gestionpfes.adnan.services.EtudiantService;
import com.gestionpfes.adnan.services.GroupeService;

import jakarta.servlet.http.HttpSession;

@Controller
public class BookingEtudiantController {


    @Autowired
private EtudiantService etudiantService;

@Autowired
private GroupeService groupeService;

@Autowired
private BookingService bookingService;


@GetMapping("/Etudiant/appointment")
public String displayRvpage(Model model , HttpSession session){

    Long userid = (Long) session.getAttribute("userID");

    Etudiant etudiant = etudiantService.getEtudiantById(userid);
    for(int i =0 ;i<7;i++){
        System.out.println("im in the controlller !!!!!!!!");
    }
    
    Groupe groupe = groupeService.findById(etudiant.getGroupeID()).orElse(null);
 
    
    if(groupe != null){
        
      if(groupe.isAutorisation()) {
        if(groupe.getPresentationDate()==null){
        List<Booking> listbookings = bookingService.getBookingsByFilierbookingAndBookedOrderByDateAsc(groupe.getFilier(),false);


        List<Booking> uniqueBookings = listbookings.stream()
    .collect(Collectors.collectingAndThen(
        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Booking::getDate))),
        ArrayList::new
    ));

    
    if(!uniqueBookings.isEmpty()){
    model.addAttribute("listbookings", uniqueBookings);
}

    return "EtudiantRV";


}else{
    Booking bookedgb = bookingService.getBookingByGroupeid(groupe.getId()).orElse(null);
    if(bookedgb!=null){

    LocalDate daterserver=bookedgb.getDate();
    LocalTime timereserver=bookedgb.getTime();
      
       
    model.addAttribute("votrerv", "Vous avez pris rendez-vous :"
    +daterserver+" ("+timereserver+")");
        return "EtudiantRV";
    }else{
        model.addAttribute("messagfail", "problemes with finding the reservation");
        return "EtudiantRV";
    }
}
}else{

    model.addAttribute("messagfail", "votre groupe n'a pas encore"+
    " obtenu l'autorisation de prendre rendez-vous ");
    return "EtudiantRV";


}
}  else{

    model.addAttribute("messagfail", "vous n'avez encore rejoint aucun groupe");
    return "EtudiantRV";
}

    
}

@GetMapping("/Etudiant/selecttime/{id}")
public String displayselecttimepage(@PathVariable("id") Long id,Model model , HttpSession session
, RedirectAttributes re)
{
    Long userid = (Long) session.getAttribute("userID");

    Etudiant etudiant = etudiantService.getEtudiantById(userid);

 Booking bookingdate = bookingService.getBookingById(id).orElse(null);

 if(bookingdate !=null)
 {
   
   
    List<Booking> boogkingslist =  bookingService
    .getBookingsByFilierbookingAndBookedAndDateOrderByTimeAsc(etudiant.getFilier()
    ,false , bookingdate.getDate());

     model.addAttribute("listbookings", boogkingslist);

     return "EtudiantselectRv";

 }
 else{
     re.addFlashAttribute("messagfail", "il y a un problème pour trouver cette date");

     return "redirect:/Etudiant/appointment";
 }
 
}

    
@PostMapping("/Etudiant/selectrendezvous")
public String saveSelectedBooking(@RequestParam("bookingId") Long bookingId,
 HttpSession session, RedirectAttributes re ) {
    // Retrieve the user ID from the session
    Long userId = (Long) session.getAttribute("userID");
 
     Etudiant etudiant = etudiantService.getEtudiantById(userId);

    Groupe groupe = groupeService.findById(etudiant.getGroupeID()).orElse(null);

    if(groupe != null){

        if(groupe.getPresentationDate()==null){
    // Retrieve the selected booking from the database
    Optional<Booking> optionalBooking = bookingService.getBookingById(bookingId);
    
    if (optionalBooking.isPresent()) {

        Booking selectedBooking = optionalBooking.get();
          
        groupeService.updateGroupePresentationDate(groupe.getId(), selectedBooking.getDate());
        // Perform any necessary checks or validations here

        // Update the booking as booked
        bookingService.updateBookingBooked(selectedBooking.getId(), true);

        // Update the groupeid as booked
        bookingService.updateBookingGroupeid(selectedBooking.getId(), groupe.getId());

        // Redirect to a success page or display a success message
        re.addFlashAttribute("messagesucces", "Booking successfully saved!");
        return "redirect:/Etudiant/appointment";
    } else {
        // Redirect to a failure page or display a failure message
        re.addFlashAttribute("messagfail", "Invalid booking ID");
        return "redirect:/Etudiant/appointment";
    }
}else{
    Booking bookedgb = bookingService.getBookingByGroupeid(groupe.getId()).orElse(null);
    if(bookedgb!=null){

    LocalDate daterserver=bookedgb.getDate();
    LocalTime timereserver=bookedgb.getTime();
      
       
    re.addFlashAttribute("messagfail", "vous n'avez deja rservr un rendez vous Le :"
    +daterserver+" ("+timereserver+")");
        return "EtudiantRV";
    }else{
        re.addFlashAttribute("messagfail", "problemes with finding the reservation");
        return "EtudiantRV";
    }
}
 }else{

        re.addFlashAttribute("messagfail", "vous n'avez encore rejoint aucun groupe");
        return "redirect:/Etudiant/appointment";
    }
}



}
