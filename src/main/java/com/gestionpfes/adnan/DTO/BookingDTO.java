package com.gestionpfes.adnan.DTO;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;


@Data
public class BookingDTO {

    private Long id;
    private LocalDate date;
    private LocalTime time;
    private Boolean booked;
    private String filierbooking;
    private String groupname;
    
}
