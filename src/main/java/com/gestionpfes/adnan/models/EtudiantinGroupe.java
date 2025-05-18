package com.gestionpfes.adnan.models;

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
import org.springframework.lang.Nullable;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name="EtudiantinGroupe")
public class EtudiantinGroupe {


    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "groupeID" , nullable = false)
    @Nullable
    private Long groupeID;
    
    @Column(name = "etudiantID" , nullable = false)
    @Nullable
    private Long etudiantID;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((groupeID == null) ? 0 : groupeID.hashCode());
        result = prime * result + ((etudiantID == null) ? 0 : etudiantID.hashCode());
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
        EtudiantinGroupe other = (EtudiantinGroupe) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (groupeID == null) {
            if (other.groupeID != null)
                return false;
        } else if (!groupeID.equals(other.groupeID))
            return false;
        if (etudiantID == null) {
            if (other.etudiantID != null)
                return false;
        } else if (!etudiantID.equals(other.etudiantID))
            return false;
        return true;
    }



    
}
