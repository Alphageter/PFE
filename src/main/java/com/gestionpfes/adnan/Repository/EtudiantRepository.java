package com.gestionpfes.adnan.Repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.gestionpfes.adnan.models.Etudiant;



public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {
   
    List<Etudiant> findAll();
    List<Etudiant> findAll(Sort sort); 
    Etudiant findById(long id);
    List<Etudiant> findByName(String name);
    List<Etudiant> findByLastname(String lastname);
    Etudiant findByEmail(String email);
    List<Etudiant> findByValidation(Boolean Validation);
    Etudiant findByNapoge(long Napoge);
    List<Etudiant> findByFilier(String filier);
    List<Etudiant> findByImage(String image);
    List<Etudiant> findByGroupeID(Long groupeID);   
    Long countByFilier (String filier);
    boolean existsByEmail(String email);
    boolean existsByNapoge(long napoge);
}
