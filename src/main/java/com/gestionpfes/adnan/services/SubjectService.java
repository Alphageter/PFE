package com.gestionpfes.adnan.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gestionpfes.adnan.Repository.SubjectRepository;
import com.gestionpfes.adnan.models.Subject;
import com.gestionpfes.adnan.models.User;

@Service
public class SubjectService {

     @Autowired 
    private final SubjectRepository subjectRepository;

    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    //GET Methods for Subject

    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    public List<Subject> getSubjectsByTimesSelected(int timesSelected) {
        return subjectRepository.findBytimesSelected(timesSelected);
    }

    public List<Subject> getSubjectsByStatus(String status) {
        return subjectRepository.findByStatus(status);
    }

    public List<Subject> getSubjectsByLanguage(String language) {
        return subjectRepository.findByLanguage(language);
    }

    public List<Subject> getSubjectsByPdfFile(String pdfFile) {
        return subjectRepository.findByPdfFile(pdfFile);
    }

    public List<Subject> getSubjectsByCreateur(User user) {
        return subjectRepository.findByCreateur(user);
    }

    public Subject getSubjectById(Long id) {
        return subjectRepository.findById(id).orElse(null);
    }

    public Subject getSubjectByName(String name) {
        return subjectRepository.findByName(name);
    }

    public List<Subject> getSubjectBytimesSelectedLessThanEqual(int MaxtimesSelected ){
        return subjectRepository.findByTimesSelectedLessThanEqual(MaxtimesSelected);
    }

    public List<Subject> getSubjectByUsertimesSelectedLessThanEqual(User createur,int MaxtimesSelected ){
        return subjectRepository.findByCreateurAndTimesSelectedLessThanEqual(createur , MaxtimesSelected );
    }
 
    public List<Subject> getSubjectByCreateurOrderByid(User createur ){
        return subjectRepository.findByCreateurOrderById(createur );
    }

    
    public List<Subject> getSubjectsByCreateurAndStatus(User createur , String status ){
        return subjectRepository.findByCreateurAndStatus(createur , status );
    }
    
    

    //UPDATE Methodes for Subject

    public Subject updateSubject(Long id, Subject updatedSubject) {
        Subject existingSubject = subjectRepository.findById(id).orElse(null);

        if (existingSubject == null) {
            return null;
        }

        existingSubject.setName(updatedSubject.getName());
        existingSubject.setTimesSelected(updatedSubject.getTimesSelected());
        existingSubject.setStatus(updatedSubject.getStatus());
        existingSubject.setPdfFile(updatedSubject.getPdfFile());
        existingSubject.setCreateur(updatedSubject.getCreateur());
        existingSubject.setLanguage(updatedSubject.getLanguage());

        return subjectRepository.save(existingSubject);
    }
    public Subject updateName(Long id, String name) {
        Subject existingSubject = subjectRepository.findById(id).orElse(null);

        if (existingSubject == null) {
            return null;
        }

        existingSubject.setName(name);

        return subjectRepository.save(existingSubject);
    }

    public Subject updateTimesSelected(Long id, int timesSelected) {
        Subject existingSubject = subjectRepository.findById(id).orElse(null);

        if (existingSubject == null) {
            return null;
        }

        existingSubject.setTimesSelected(timesSelected);

        return subjectRepository.save(existingSubject);
    }

    public Subject updateStatus(Long id, String status) {
        Subject existingSubject = subjectRepository.findById(id).orElse(null);

        if (existingSubject == null) {
            return null;
        }

        existingSubject.setStatus(status);

        return subjectRepository.save(existingSubject);
    }

    public Subject updatePdfFile(Long id, String pdfFile) {
        Subject existingSubject = subjectRepository.findById(id).orElse(null);

        if (existingSubject == null) {
            return null;
        }

        existingSubject.setPdfFile(pdfFile);

        return subjectRepository.save(existingSubject);
    }

    public Subject updateCreateur(Long id, User createur) {
        Subject existingSubject = subjectRepository.findById(id).orElse(null);

        if (existingSubject == null) {
            return null;
        }

        existingSubject.setCreateur(createur);

        return subjectRepository.save(existingSubject);
    }

    //CREATE Methodes for Subject

    public Subject createSubject(Subject subject) {
        return subjectRepository.save(subject);
    }


    //DELETE SUbject Methodes

    public void deleteSubjectById(Long id) {
        subjectRepository.deleteById(id);
    }

    public Subject removePdfFile(Long id) {
        Subject existingSubject = subjectRepository.findById(id).orElse(null);
        
    
        if (existingSubject == null) {
            return null;
        }
    
        existingSubject.setPdfFile(null);
    
        return subjectRepository.save(existingSubject);
    }


    //SEARCHE Method
    public List<Subject> searchSubject(String search) {
        List<Subject> subjects = new ArrayList<>();
    
        // Search subjects by name
        List<Subject> subjectsByName = subjectRepository.findByNameContainingIgnoreCase(search);
        subjects.addAll(subjectsByName);
    
        // Search subjects by language
        List<Subject> subjectsByLanguage = subjectRepository.findByLanguageContainingIgnoreCase(search);
        subjects.addAll(subjectsByLanguage);
    
        return subjects;
    }
}
