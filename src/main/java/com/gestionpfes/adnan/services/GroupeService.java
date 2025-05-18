package com.gestionpfes.adnan.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import com.gestionpfes.adnan.Repository.GroupeRepository;
import com.gestionpfes.adnan.handleErrores.handleErrortest.EntityNotFoundException;
import com.gestionpfes.adnan.models.Groupe;

@Service

public class GroupeService {

    @Autowired
    private GroupeRepository groupeRepository;


    //get Methods

    public Optional<Groupe> findById(Long id) {
        return groupeRepository.findById(id);
    }

    
    public List<Groupe> findByEndarantIDAndStatus(Long encadrantid , String status) {
        return groupeRepository.findByEndarantIDAndStatus(encadrantid , status);
    }

    public List<Groupe> findByEndarantID(Long encadrantid) {
        return groupeRepository.findByEndarantID(encadrantid);
    }

    public Groupe findByName(String name) {
        return groupeRepository.findByName(name);
    }

    public List<Groupe> findByStatus(String status) {
        return groupeRepository.findByStatus(status);
    }

    public List<Groupe> findByTypeOfWork(String typeOfWork) {
        return groupeRepository.findByTypeOfWork(typeOfWork);
    }

    public List<Groupe> findBySubjectID(Long subjectid) {
        return groupeRepository.findBySubjectID(subjectid);
    }

    public List<Groupe> findByPresentationDate(LocalDate date) {
        return groupeRepository.findByPresentationDate(date);
    }

    public List<Groupe> findByAutorisation(boolean autorisation) {
        return groupeRepository.findByAutorisation(autorisation);
    }

    public List<Groupe> findByFilier(String filier){
        return groupeRepository.findByFilier(filier);
    }

    public List<Groupe> findAllByOrderByIdAsc() {
        return groupeRepository.findAllByOrderByIdAsc();
    }

    public List<Groupe> findAll() {
        return groupeRepository.findAll();
    }

    public List<Groupe> findAll(Sort sort) {
        return groupeRepository.findAll(sort);
    }

    public boolean existsByName(String name) {
        return groupeRepository.existsByName(name);
    }

    public Groupe createGroupe(Groupe groupe) {
        return groupeRepository.save(groupe);
    }
    
     //update methods

    public Groupe updateGroupe(Long id, Groupe updatedGroupe) {
        Optional<Groupe> optionalGroupe = groupeRepository.findById(id);
        if (optionalGroupe.isPresent()) {
            Groupe groupe = optionalGroupe.get();
            groupe.setName(updatedGroupe.getName());
            groupe.setStatus(updatedGroupe.getStatus());
            groupe.setTypeOfWork(updatedGroupe.getTypeOfWork());
            groupe.setAutorisation(updatedGroupe.isAutorisation());
            groupe.setEndarantID(updatedGroupe.getEndarantID());
            groupe.setSubjectID(updatedGroupe.getSubjectID());
            groupe.setPresentationDate(updatedGroupe.getPresentationDate());
            return groupeRepository.save(groupe);
        } else {
            throw new RuntimeException("Groupe not found with id: " + id);
        }
    }

    public Groupe updateGroupeinfo(Long id, Groupe updatedGroupe) {
        Optional<Groupe> optionalGroupe = groupeRepository.findById(id);
        if (optionalGroupe.isPresent()) {
            Groupe groupe = optionalGroupe.get();
            groupe.setName(updatedGroupe.getName());
            groupe.setStatus(updatedGroupe.getStatus());
            groupe.setTypeOfWork(updatedGroupe.getTypeOfWork());
            groupe.setAutorisation(updatedGroupe.isAutorisation());
            return groupeRepository.save(groupe);
        } else {
            throw new RuntimeException("Groupe not found with id: " + id);
        }
    }

    public Groupe updateGroupeName(Long id, String name) {
        Groupe groupe = groupeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Groupe not found with id " + id));
        groupe.setName(name);
        return groupeRepository.save(groupe);
    }

    public Groupe updateGroupeStatus(Long id, String status) {
        Groupe groupe = groupeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Groupe not found with id " + id));
        groupe.setStatus(status);
        return groupeRepository.save(groupe);
    }

    public Groupe updateGroupeTypeOfWork(Long id, String typeOfWork) {
        Groupe groupe = groupeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Groupe not found with id " + id));
        groupe.setTypeOfWork(typeOfWork);
        return groupeRepository.save(groupe);
    }

    public Groupe updateGroupePresentationDate(Long id, LocalDate presentationDate) {
        Groupe groupe = groupeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Groupe not found with id " + id));
        groupe.setPresentationDate(presentationDate);
        return groupeRepository.save(groupe);
    }

    public Groupe updateGroupeAutorisation(Long id, boolean autorisation) {
        Groupe groupe = groupeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Groupe not found with id " + id));
        groupe.setAutorisation(autorisation);
        return groupeRepository.save(groupe);
    }

    public Groupe updateGroupeFilier(Long id, String filier) {
        Groupe groupe = groupeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Groupe not found with id " + id));
        groupe.setFilier(filier);
        return groupeRepository.save(groupe);
    }

    public Groupe updateGroupeSubjectID(Long id, Long subjectID) {
        Groupe groupe = groupeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Groupe not found with id " + id));
        groupe.setSubjectID(subjectID);
        return groupeRepository.save(groupe);
    }

    public Groupe updateGroupeEndarantID(Long id, Long endarantID) {
        Groupe groupe = groupeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Groupe not found with id " + id));
                groupe.setEndarantID(endarantID);
        return groupeRepository.save(groupe);
    }


   //Delete methods

    public void deleteGroupe(Long id) {
        Optional<Groupe> optionalGroupe = groupeRepository.findById(id);
        if (optionalGroupe.isPresent()) {
            groupeRepository.deleteById(id);
        } else {
            throw new RuntimeException("Groupe not found with id: " + id);
        }
    }


     //create new Encadrant

     public void save(Groupe groupe){
        groupeRepository.save(groupe);
    }

    //check if existe
    public boolean ExisteById(long id){
        return groupeRepository.existsById(id);
    }
    public boolean ExisteByName(String name){
        return groupeRepository.existsByName(name);
    }

}