package com.gestionpfes.adnan.models;

import java.time.LocalDate;
import java.util.Objects;

import org.springframework.lang.Nullable;

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
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name="Groupe")
public class Groupe {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "endarantID")
    @Nullable
    private Long endarantID;
    
    @Column(name = "name", nullable = false, unique = true)
    private String name;
    
    
    @Column(name = "status" , nullable = false)
    private String status="en attente";
    
    @Column(name = "typeOfWork" , nullable = false , columnDefinition = "VARCHAR(255) DEFAULT 'individual'")
    private String typeOfWork="individual";
    
    
    @Column(name = "subjectID")
    @Nullable
    private Long subjectID;
    
    @Column(name = "presentationDate")
    @Nullable
    private LocalDate presentationDate;

    @Column(name = "autorisation" , nullable = false)
    private boolean autorisation=false;

    @Column(name = "filier", nullable = false)
    private String filier;

    

    @Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Groupe)) return false;
    Groupe groupe = (Groupe) o;
    return Objects.equals(getId(), groupe.getId());
}

@Override
public int hashCode() {
    return Objects.hash(getId());
}
}
