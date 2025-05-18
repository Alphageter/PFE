package com.gestionpfes.adnan.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.gestionpfes.adnan.models.Booking;





public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    List<Booking> findAll();

    List<Booking> findAll(Sort sort);

    List<Booking> findByFilierbookingAndBookedOrderByTimeAsc(String filierbooking, Boolean booked);
    
    List<Booking> findByFilierbookingAndBookedOrderByDateAsc(String filierbooking, Boolean booked);

    List<Booking> findByFilierbooking(String filierbooking);

    List<Booking> findByDate(LocalDate date);

    List<Booking> findByTime(LocalTime time);

    List<Booking> findByBooked(Boolean booked);

    Optional<Booking> findByGroupeid(Long groupeid);


    List<Booking> findByBookedOrderByFilierbookingAsc(Boolean booked);

    List<Booking> findByFilierbookingOrderByDateAsc(String filierbooking);
   
    List<Booking> findByFilierbookingAndBookedAndDateOrderByTimeAsc(String filierbooking, boolean booked, LocalDate date);

    List<Booking> findByBookedOrderByFilierbookingAscDateAscTimeAsc(Boolean booked);
}