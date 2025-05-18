package com.gestionpfes.adnan.models;



import java.util.Objects;

import org.springframework.lang.Nullable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
@Table(name="Subject")
public class Subject  {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @Nullable
    private User createur;

    

    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "times_selected", nullable = false)
    private int timesSelected=0;

    @Column(name = "status", nullable = false)
    private String status ="en attente";
    
    @Column(name = "language", nullable = false)
    private String language;

    @Column(name = "pdf_file")
    private String pdfFile;

    

    

    @Override
public boolean equals(Object obj) {
    if (this == obj) {
        return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
        return false;
    }
    Subject subject = (Subject) obj;
    return Objects.equals(id, subject.id) &&
           Objects.equals(name, subject.name) &&
           Objects.equals(createur, subject.createur) &&
           timesSelected == subject.timesSelected &&
           Objects.equals(status, subject.status) &&
           Objects.equals(language, subject.language) &&
           Objects.equals(pdfFile, subject.pdfFile);
}

public Subject() {
    this.createur=null;
    this.language=null;
    this.name=null;
    this.status="en attente";
    this.timesSelected=0;
    this.pdfFile=null;
    }

@Override
public int hashCode() {
    return Objects.hash(id, name, createur, timesSelected, status , language, pdfFile);
}
}
