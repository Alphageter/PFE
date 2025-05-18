package com.gestionpfes.adnan.models;

import java.util.Objects;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;

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
public class Etudiant extends User {
    
    @Column(name = "napoge", nullable = false , unique = true)
    private Long napoge;
    
    @Column(name = "filier", nullable = false)
    private String filier;

    @Column(name = "groupeID")
             private Long groupeID;




    public Etudiant(){
        this.setRole("etudiant");
        this.setImage("/images/annonyme.jpg");
    }
    

    @Override
public boolean equals(Object obj) {
    if (obj == this) {
        return true;
    }
    if (!(obj instanceof Etudiant)) {
        return false;
    }
    Etudiant etudiant = (Etudiant) obj;
    return Objects.equals(getId(), etudiant.getId())
            && Objects.equals(getName(), etudiant.getName())
            && Objects.equals(getLastname(), etudiant.getLastname())
            && Objects.equals(getEmail(), etudiant.getEmail())
            && Objects.equals(getPassword(), etudiant.getPassword())
            && Objects.equals(isValidation(), etudiant.isValidation())
            && Objects.equals(getRole(), etudiant.getRole())
            && Objects.equals(getNapoge(), etudiant.getNapoge())
            && Objects.equals(getFilier(), etudiant.getFilier());
}

@Override
public int hashCode() {
    return Objects.hash(getId(), getName(), getLastname(), getEmail(), getPassword(), isValidation(),
            getRole(), getNapoge(), getFilier());
}

}
