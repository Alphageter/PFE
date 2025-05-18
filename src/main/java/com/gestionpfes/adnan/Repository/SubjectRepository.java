package com.gestionpfes.adnan.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import com.gestionpfes.adnan.models.Subject;
import com.gestionpfes.adnan.models.User;


public interface SubjectRepository extends JpaRepository<Subject, Long> {

    List<Subject> findAll();
    List<Subject> findBytimesSelected(int timesSelected );
    List<Subject> findByStatus(String Status);
    List<Subject> findByLanguage(String language);
    List<Subject> findByPdfFile(String pdffile);
    List<Subject> findByCreateur(User user);
    Subject findByName(String name);
    Optional<Subject> findById(Long id);
    List<Subject> findByTimesSelectedLessThanEqual(int maxTimesSelected);
    List<Subject> findByCreateurAndTimesSelectedLessThanEqual(User user, int maxTimesSelected);
    List<Subject> findByNameContainingIgnoreCase(String name);
    List<Subject> findByLanguageContainingIgnoreCase(String language);
    List<Subject> findByCreateurAndStatus(User createur , String status);
    List<Subject> findByCreateurOrderById(User createur);

    

}
