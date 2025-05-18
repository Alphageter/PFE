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
public class ModifierBookingController {

    @Autowired
private BookingService bookingService;

@Autowired
private GroupeService groupeService;



@GetMapping("/Admin/deleteBooking/{bookingId}")
public String deleteBooking(@PathVariable("bookingId") Long bookingId, RedirectAttributes re) {
    // Retrieve the booking by its ID
    Booking booking = bookingService.getBookingById(bookingId).orElse(null);
    for(int i =0 ;i<7;i++){
        System.out.println("im in the delete controlller second Step !!!!!!!!");
    }
    // Check if the booking exists
    if (booking != null) {
        // Retrieve the associated group
        if(booking.getGroupeid()!=null)
        {
        Groupe groupe = groupeService.findById(booking.getGroupeid()).orElse(null) ;
         // Update the presentation date of the associated group
        groupeService.updateGroupePresentationDate(groupe.getId(), null);
    }
        // Delete the booking
        bookingService.deleteBooking(bookingId);

       
        
        
        // Redirect to the booking list page or display a success message
        re.addFlashAttribute("messagesucces", "Le rendez-vous a été Supprimer avec succès.");
    } else {
        // Redirect to the booking list page or display an error message
        re.addFlashAttribute("messagfail", "Le rendez-vous n'existe pas.");
    }

    return "redirect:/Admin/modifierRV";
}








    
}
