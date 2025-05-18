package com.gestionpfes.adnan.models;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
// import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
// @NoArgsConstructor
@AllArgsConstructor
@ToString
@PrimaryKeyJoinColumn(name="id")

@Table(name="Encadrant")
public class Encadrant extends User {
    
    @Column(name = "groupesnumber", nullable = false)
    private int groupesnumber;
    
    @Column(name = "filier", nullable = false)
    private String filier;




    public Encadrant(){
        this.setRole("encadrant");
        this.setGroupesnumber(0);
        this.setImage("/images/annonyme.jpg");
    }
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Encadrant)) {
            return false;
        }
        Encadrant encadrant = (Encadrant) obj;
        return Objects.equals(getId(), encadrant.getId())
                && Objects.equals(getName(), encadrant.getName())
                && Objects.equals(getLastname(), encadrant.getLastname())
                && Objects.equals(getEmail(), encadrant.getEmail())
                && Objects.equals(getPassword(), encadrant.getPassword())
                && Objects.equals(isValidation(), encadrant.isValidation())
                && Objects.equals(getRole(), encadrant.getRole())
                && getGroupesnumber() == encadrant.getGroupesnumber()
                && Objects.equals(getFilier(), encadrant.getFilier());
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getLastname(), getEmail(), getPassword(), isValidation(),
                getRole(), getGroupesnumber(), getFilier());
    }

}
