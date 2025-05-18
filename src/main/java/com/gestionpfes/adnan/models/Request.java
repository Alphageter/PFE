package com.gestionpfes.adnan.models;



import java.util.Objects;

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
@AllArgsConstructor
@ToString
@Table(name = "request")
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "subject" , nullable = false)
    private String subject;

  
    @Column(name = "status" , nullable = false)
    private String status= "en attente";

    @Column(name = "seen" , nullable = false)
    private boolean seen= false;

    @Column(name = "userGeterId" , nullable = false)
    private Long userGeterId;
    
    @Column(name = "userSenderId" , nullable = false)
    private Long userSenderId;

    

    // public Request(String subject, Etudiant etudiant, Encadrant encadrant) {
    //     this.subject = subject;
    //     this.etudiant = etudiant;
    //     this.encadrant = encadrant;
    //     this.status = "en attente";
    // }

    // public Request() {
    //     this.status = "en attente";
    // }

    // getters and setters

    // equals and hashCode methods

    @Override
     public boolean equals(Object o) {
         if (this == o)
          return true; 
          if (!(o instanceof Request)) 
          return false;
           Request request = (Request) o;
            return Objects.equals(getId(), request.getId())
             && Objects.equals(getSubject(), request.getSubject())
             && Objects.equals(getUserGeterId(), request.getUserGeterId())
             && Objects.equals(getUserSenderId(), request.getUserSenderId()); 
        } 
        @Override
         public int hashCode() {
             return Objects.hash(getId(), getSubject(), getUserGeterId(), getUserSenderId());
             }
}