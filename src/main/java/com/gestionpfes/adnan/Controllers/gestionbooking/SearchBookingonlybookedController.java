package com.gestionpfes.adnan.Controllers.gestionbooking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gestionpfes.adnan.DTO.BookingDTO;
import com.gestionpfes.adnan.models.Booking;
import com.gestionpfes.adnan.models.Groupe;
import com.gestionpfes.adnan.services.BookingService;
import com.gestionpfes.adnan.services.GroupeService;

@Controller
public class SearchBookingonlybookedController {



    @Autowired
private BookingService bookingService;

@Autowired
private GroupeService groupService;


@PostMapping("/Admin/searchBookingbooked")
public String searchBooking(@RequestParam("search") String search,
                            Model model,
                            RedirectAttributes re) {
                                List<Booking> searchResults = new ArrayList<>();                           
    
                                for(int i =0 ;i<7;i++){
                                    System.out.println("im in the controlller second Step !!!!!!!!");
                                }
                                // Search by Filierbooking
                                if (!search.isEmpty()) {
                                    searchResults = bookingService.getBookingsByFilierbooking(search);
                                } 
                                // Search by ID
                                else if (NumberUtils.isParsable(search)) {
                                    Long bookingId = Long.parseLong(search);
                                    Booking booking = bookingService.getBookingById(bookingId).orElse(null);
                                    if(booking!=null){
                                    searchResults.add(booking);
                                }
                                }
                                // Search by Groupe Name
                                else {
                                    Groupe groupesearched = groupService.findByName(search);
                                    if(groupesearched !=null){
                                        Booking booked = bookingService.getBookingByGroupeid(groupesearched.getId()).orElse(null);
                                    if(booked!=null){
                                        searchResults.add(booked);;
                                                     }         
                                    }
                                }
                            

    // Filter by validation status
final Boolean bookedFilter = true;

// Apply the booked filter
if (bookedFilter != null) {
    final Boolean finalBookedFilter = bookedFilter;
    searchResults = searchResults.stream()
            .filter(booking -> booking.getBooked().equals(finalBookedFilter))
            .collect(Collectors.toList());
}


// Sort by filierbooking
Collections.sort(searchResults, Comparator.comparing(Booking::getFilierbooking)
.thenComparing(Booking::getDate).thenComparing(Booking::getTime));

List<BookingDTO> listBookingDTOs = new  ArrayList<>();
  
    for(Booking booking : searchResults)
    {
        BookingDTO bookingDto=new BookingDTO();
    
       if(booking.getGroupeid()!=null){  
                   
        Groupe groupe =groupService.findById(booking.getGroupeid()).orElse(null);
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

    // Check if search results are empty
    if (searchResults.isEmpty()) {
        re.addFlashAttribute("messagfail", "Aucun résultat trouvé pour la recherche spécifiée.");
        return "redirect:/Admin/listrendezvous";
    }

    model.addAttribute("listBookings", searchResults);
    return "listBookingbooked";
}

    
}
