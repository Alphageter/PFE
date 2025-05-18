package com.gestionpfes.adnan.models;

import java.time.LocalDate;
import java.time.LocalTime;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Table(name = "Booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "time", nullable = false)
    private LocalTime time;

    @Column(name="booked" , nullable = false)
    private Boolean booked = false ;

    @Column( name = "filier" , nullable = false)
    private String filierbooking ;

    @Column( name = "groupeid" , nullable = true)
    private Long groupeid;

    // Constructors, getters, setters, and other methods


    // public String getGroupName() {
    //     if (group != null) {
    //         return group.getName();
    //     }
    //     return null;
    // }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result + ((time == null) ? 0 : time.hashCode());
        result = prime * result + ((booked == null) ? 0 : booked.hashCode());
        result = prime * result + ((filierbooking == null) ? 0 : filierbooking.hashCode());
       
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Booking other = (Booking) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (date == null) {
            if (other.date != null)
                return false;
        } else if (!date.equals(other.date))
            return false;
        if (time == null) {
            if (other.time != null)
                return false;
        } else if (!time.equals(other.time))
            return false;
        if (booked == null) {
            if (other.booked != null)
                return false;
        } else if (!booked.equals(other.booked))
            return false;
        if (filierbooking == null) {
            if (other.filierbooking != null)
                return false;
        } else if (!filierbooking.equals(other.filierbooking))
            return false;
        return true;
    }
   

  
   
    // Add any other relevant attributes

    
    // Override equals and hashCode if needed
}