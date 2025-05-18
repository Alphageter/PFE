package com.gestionpfes.adnan.Controllers.gestionbooking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gestionpfes.adnan.models.Booking;
import com.gestionpfes.adnan.models.Groupe;
import com.gestionpfes.adnan.services.BookingService;
import com.gestionpfes.adnan.services.GroupeService;

@Controller
public class CanselBookingController {

@Autowired
private BookingService bookingService;

@Autowired
private GroupeService groupeService;

@GetMapping("/Admin/canselBooking/{bookingId}")
public String cancelBooking(@PathVariable("bookingId") Long bookingId ,
                               RedirectAttributes re) {
    // Retrieve the booking by its ID
    Booking booking = bookingService.getBookingById(bookingId).orElse(null);

    // Check if the booking exists
    if (booking != null) {
        // Set the booked property to false
        booking.setBooked(false);

        // Save the updated booking
        bookingService.saveBooking(booking);
         
        bookingService.updateBookingGroupeid(booking.getId(), null);
        
        Groupe groupeupdate = groupeService.findById(booking.getGroupeid()).orElse(null);

        if (groupeupdate != null) {
            
            groupeService.updateGroupePresentationDate(groupeupdate.getId(), null);


        }else{
                
            re.addFlashAttribute("messagfail", "there is trouble with finding the groupe");
            return "redirect:/Admin/listrendezvous";

        }

    }
    else{
                
        re.addFlashAttribute("messagfail", "there is trouble with finding the appointment");
        return "redirect:/Admin/listrendezvous";

    }

    // Redirect to the booking list page or display a success message
    re.addFlashAttribute("messagesucces", "le rendez-vous réservé a été annulé avec succès");
        
    return "redirect:/Admin/listrendezvous";
}
    
}
