package com.gestionpfes.adnan.Controllers.gestionbooking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.gestionpfes.adnan.DTO.BookingDTO;
import com.gestionpfes.adnan.models.Booking;
import com.gestionpfes.adnan.models.Groupe;
import com.gestionpfes.adnan.services.BookingService;
import com.gestionpfes.adnan.services.GroupeService;

@Controller
public class BookingCategorie {
    
@Autowired
private BookingService bookingService;

@Autowired
private GroupeService groupeService;

@GetMapping("/Admin/appointments")
public String displaycategoriepage(Model model){

    return "categorieRV";
}





@GetMapping("/Admin/proposeRV")
public String displayproposeRVpage(Model model)
{
    List<String> listFilier = new ArrayList<>(Arrays.asList("SMI", "SMA", "SMP", "SMC", "SVI","STU"));
List<String> usedFilier = new ArrayList<>();

for (String filier : listFilier) {
    List<Booking> listBookings = bookingService.getBookingsByFilierbooking(filier);
    if (!listBookings.isEmpty()) {

        // Add the used filier to the usedFilier list
        usedFilier.add(filier);
    }
}

// Remove the used filier from the original listFilier
listFilier.removeAll(usedFilier);

model.addAttribute("listFilier", listFilier);
    
    return "newBooking";
}


//list Rondez_vous reserver

@GetMapping("/Admin/listrendezvous")
public String displaylistbookedRVpage(Model model)
{
    List<Booking> listBookings = bookingService.getByBookedOrderByFilierbookingAscDateAscTimeAsc(true);
   
    List<BookingDTO> listBookingDTOs = new  ArrayList<>();
    for(Booking booking : listBookings)
    {
        BookingDTO bookingDto=new BookingDTO();
        if(booking.getGroupeid()!=null){  
                   
            Groupe groupe =groupeService.findById(booking.getGroupeid()).orElse(null);
             if(groupe!=null)
         {
            bookingDto.setGroupname(groupe.getName());
         }
     }
         else{
             bookingDto.setGroupname("vide");
         }
        bookingDto.setId(booking.getId());
        bookingDto.setDate(booking.getDate());
        bookingDto.setTime(booking.getTime());
        bookingDto.setBooked(booking.getBooked());
        bookingDto.setFilierbooking(booking.getFilierbooking());
       
        listBookingDTOs.add(bookingDto);

    }
    
    
    model.addAttribute("listBookings", listBookingDTOs);
    return "listBookingbooked";
}


//Listt of all Rendez_vous  

@GetMapping("/Admin/modifierRV")
public String displaymodificationRVpage(Model model)
{
for(int i =0 ;i<7;i++){
    System.out.println("im in the controlller !!!!!!!!");
}

           
       
     List<Booking> listBookings = bookingService.getAllBookingsSorted(Sort.by(
        Sort.Order.asc("filierbooking"),
        Sort.Order.asc("date"),
        Sort.Order.asc("time")));
        for(int i =0 ;i<7;i++){
            System.out.println("im in the controlller second Step !!!!!!!!");
        }
            
        List<BookingDTO> listBookingDTOs = new  ArrayList<>();
        
   
        for(Booking booking : listBookings)
    {
       
        BookingDTO bookingDto=new BookingDTO();
       
        if(booking.getGroupeid()!=null){  
                   
           Groupe groupe =groupeService.findById(booking.getGroupeid()).orElse(null);
            if(groupe!=null)
        {
           bookingDto.setGroupname(groupe.getName());
        }
    }
        else{
            bookingDto.setGroupname("vide");
        }
        bookingDto.setId(booking.getId());
        bookingDto.setDate(booking.getDate());
        bookingDto.setTime(booking.getTime());
        bookingDto.setBooked(booking.getBooked());
        bookingDto.setFilierbooking(booking.getFilierbooking());
       
        listBookingDTOs.add(bookingDto);

    

    }
        for(int i =0 ;i<7;i++){
            System.out.println("and we sorted the list !!!!!!!!");
        }
     model.addAttribute("listBookings", listBookingDTOs);
     return "listBookingall";
}






}
