package com.gestionpfes.adnan.DTO;

import org.springframework.stereotype.Service;

import com.gestionpfes.adnan.models.Booking;

@Service
public class BookingDTOService  {

    // Inject any necessary dependencies here

    public BookingDTO createBookingDTO(Booking booking) {
        BookingDTO bookingDTO = new BookingDTO();
        // Map properties from Booking entity to BookingDTO
        bookingDTO.setId(booking.getId());
        bookingDTO.setDate(booking.getDate());
        bookingDTO.setTime(booking.getTime());
        bookingDTO.setBooked(booking.getBooked());
        bookingDTO.setFilierbooking(booking.getFilierbooking());

        // Additional mapping logic if needed

        return bookingDTO;
    }

    // Define other service methods as per your requirements
}
