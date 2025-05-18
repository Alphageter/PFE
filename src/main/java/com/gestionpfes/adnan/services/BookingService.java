package com.gestionpfes.adnan.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.gestionpfes.adnan.Repository.BookingRepository;
import com.gestionpfes.adnan.models.Booking;


@Service
public class BookingService {
    
    
    @Autowired
    private  BookingRepository bookingRepository;

   
    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

   //create booking

    public Booking saveBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

 //get methods
 

 public List<Booking> getAllBookings() {
    return bookingRepository.findAll();
}

public List<Booking> getAllBookingsSorted(Sort sort) {
    return bookingRepository.findAll(sort);
}

public Optional<Booking> getBookingById(Long id){

    return bookingRepository.findById(id);
}

public Optional<Booking> getBookingByGroupeid(Long groupeid){

    return bookingRepository.findByGroupeid(groupeid);
}




public List<Booking> getBookingsByDate(LocalDate date) {
    return bookingRepository.findByDate(date);
}

public List<Booking> getBookingsByTime(LocalTime time) {
    return bookingRepository.findByTime(time);
}

public List<Booking> getBookingsByBooked(Boolean booked) {
    return bookingRepository.findByBooked(booked);
}

public List<Booking> getBookingsByBookedOrderbyFilier(Boolean booked) {
    return bookingRepository.findByBookedOrderByFilierbookingAsc(booked);
}



public List<Booking> getBookingsByFilierbooking(String filierbooking) {
    return bookingRepository.findByFilierbooking(filierbooking);
}

//search method




//diplay my list


public List<Booking> getByBookedOrderByFilierbookingAscDateAscTimeAsc(Boolean booked) {
    return bookingRepository.findByBookedOrderByFilierbookingAscDateAscTimeAsc(booked);
}

//display calendery

//thisss
public List<Booking> getBookingsByFilierbookingOrderByDateAsc(String filierbooking) {
    return bookingRepository.findByFilierbookingOrderByDateAsc(filierbooking);
}

//orthisssssss

public List<Booking> getBookingsByFilierbookingAndBookedOrderByDateAsc(String filierbooking, Boolean booked) {
    return bookingRepository.findByFilierbookingAndBookedOrderByDateAsc(filierbooking, booked);
}

//forselectin time


public List<Booking> getBookingsByFilierbookingAndBookedAndDateOrderByTimeAsc(String filierbooking, Boolean booked , LocalDate date) {
    return bookingRepository.findByFilierbookingAndBookedAndDateOrderByTimeAsc(filierbooking, booked , date) ;
}

//update Booking

public Booking updateBooking(long id, Booking bookingupdate) {
    Booking booking = bookingRepository.findById(id).orElse(null);
    if(booking !=null){
        booking.setDate(bookingupdate.getDate());
        booking.setTime(booking.getTime());
        booking.setFilierbooking(bookingupdate.getFilierbooking());
        booking.setBooked(bookingupdate.getBooked());
    return bookingRepository.save(booking);
    }else{
        return null ;
    }   
}

public Booking updateBookingDate(long id, LocalDate date) {
    Booking booking = bookingRepository.findById(id).orElse(null);
    if(booking !=null){
        booking.setDate(date);
    return bookingRepository.save(booking);
    }else{
        return null ;
    }   
}

public Booking updateBookingTime(long id, LocalTime time) {
    Booking booking = bookingRepository.findById(id).orElse(null);
    if(booking !=null){
        booking.setTime(time);
    return bookingRepository.save(booking);
    }else{
        return null ;
    }   
}

public Booking updateBookingBooked(long id , Boolean booked) {
    Booking booking = bookingRepository.findById(id).orElse(null);
    if(booking !=null){
        booking.setBooked(booked);
    return bookingRepository.save(booking);
    }else{
        return null ;
    }   
}

public Booking updateBookingGroupeid(long id , Long groupeid) {
    Booking booking = bookingRepository.findById(id).orElse(null);
    if(booking !=null){
        booking.setGroupeid(groupeid);
    return bookingRepository.save(booking);
    }else{
        return null ;
    }   
}


//delet booking

    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }
}