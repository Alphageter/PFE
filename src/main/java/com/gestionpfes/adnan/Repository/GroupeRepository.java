package com.gestionpfes.adnan.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import com.gestionpfes.adnan.models.Groupe;


public interface GroupeRepository extends JpaRepository<Groupe, Long> {

    Optional<Groupe> findById(Long id);
    
    List<Groupe> findByEndarantID(Long encadrantid);

    List<Groupe> findByEndarantIDAndStatus(Long endarantID, String status);
    
    Groupe findByName(String name);
    
    List<Groupe> findByStatus(String status);
    
    List<Groupe> findByTypeOfWork(String typeOfWork);
    
    List<Groupe> findBySubjectID(Long subjectid);
    
    List<Groupe> findByPresentationDate(LocalDate date);
    
    List<Groupe> findByAutorisation(boolean autorisation);
    
    List<Groupe> findByFilier(String filier);
    
    List<Groupe> findAllByOrderByIdAsc();
    
    List<Groupe> findAll();
   
    List<Groupe> findAll(Sort sort);
    
    boolean existsByName(String name);


    }
