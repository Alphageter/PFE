package com.gestionpfes.adnan.Repository;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.gestionpfes.adnan.models.EtudiantinGroupe;

public interface EtudiantinGroupeRepository extends JpaRepository< EtudiantinGroupe , Long> {


    List<EtudiantinGroupe> findAll();
    List<EtudiantinGroupe> findAll(Sort sort); 
    EtudiantinGroupe findById(long id);
    EtudiantinGroupe findByEtudiantID(long etudiantID);
    List<EtudiantinGroupe> findByGroupeID(long groupeID);
    boolean existsByEtudiantID(long groupeID);
    void deleteByEtudiantID(Long etudiantID);
    void deleteByGroupeID(Long groupeID);
    
}
