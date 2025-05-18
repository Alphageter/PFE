package com.gestionpfes.adnan.Controllers.gestionbooking;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gestionpfes.adnan.models.Booking;
import com.gestionpfes.adnan.services.BookingService;

@Controller
public class BookingCreate {
    
@Autowired
private BookingService bookingService ;

@PostMapping("/Admin/saveRV")
public String saveRv(@RequestParam("filier") String filier,
                     @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                     @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                     RedirectAttributes re) {
    // Calculate the number of days between start date and end date
    long days = ChronoUnit.DAYS.between(startDate, endDate);

    // Check if the number of days is greater than 20
    if (days > 20) {
        // Redirect to a failure page or display a failure message
        re.addFlashAttribute("messagfail", "Le domaine temporel spécifié est trop grand");

        return "redirect:/Admin/proposeRV";
    }

    
    // Check if the start date is lower than today's date
    if (startDate.isBefore(LocalDate.now())) {
        // Redirect to a failure page or display a failure message
        re.addFlashAttribute("messagfail", "La date de début doit être supérieure ou égale à la date d'aujourd'hui");

        return "redirect:/Admin/proposeRV";
    }
    //verify si start date est superier
    if (startDate.isAfter(endDate)) {
        // Redirect to a failure page or display a failure message
        re.addFlashAttribute("messagfail", "La date de début est supérieure à la date de fin");
        return "redirect:/Admin/proposeRV";
    }
     // Loop through the dates from start date to end date
     LocalDate currentDate = startDate;
     while (!currentDate.isAfter(endDate)) {
         // Check if the current date is not a Sunday
         if (currentDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
             // Loop through the time from 9am to 6pm
             LocalTime startTime = LocalTime.of(9, 0);
             LocalTime endTime = LocalTime.of(18, 0);
             LocalTime currentTime = startTime;
             while (!currentTime.isAfter(endTime)) {
                 // Skip the 1 pm time
                 if (currentTime.getHour() != 13) {
                     // Create a new Booking entity
                     Booking booking = new Booking();
                     booking.setDate(currentDate);
                     booking.setTime(currentTime);
                     booking.setBooked(false);
                     booking.setFilierbooking(filier);
                     booking.setGroupeid(null);
 
                     // Save the booking using the bookingService or repository
                     bookingService.saveBooking(booking);
                 }
 
                 // Increment the current time by 1 hour
                 currentTime = currentTime.plusHours(1);
             }
         }
 
         // Increment the current date by 1 day
         currentDate = currentDate.plusDays(1);
     }
       re.addFlashAttribute("messagesucces", "les rendez-vous sont disponibles pour le candidat de filière : "+filier);
     // Redirect to a success page or display a success message
     return "redirect:/Admin/proposeRV";

                     }


}
