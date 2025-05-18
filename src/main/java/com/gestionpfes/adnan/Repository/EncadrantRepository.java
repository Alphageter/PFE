package com.gestionpfes.adnan.Repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.gestionpfes.adnan.models.Encadrant;



public interface EncadrantRepository extends JpaRepository<Encadrant, Long> {
    
    // List<Encadrant> findAll();
    List<Encadrant> findAll(Sort sort);
    Encadrant findById(long id);
    List<Encadrant>findByName(String name);
    List<Encadrant> findByLastname(String lastname);
    Encadrant findByEmail(String email);
    List<Encadrant> findByValidation(Boolean validation);
    List<Encadrant> findByFilier(String filier);
    List<Encadrant> findByGroupesnumber(int groupesnumber);
    List<Encadrant> findByImage(String image);
    boolean existsByEmail(String email);
    List<Encadrant> findByFilierAndGroupesnumberLessThanEqual(String filiere, int maxGroupes);
}

