package com.gestionpfes.adnan.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.gestionpfes.adnan.Repository.EncadrantRepository;
import com.gestionpfes.adnan.models.Encadrant;

@Service
public class EncadrantService {

    @Autowired
    private EncadrantRepository encadrantRepository;
     
    //get methods EncadrantServices

    

    public List<Encadrant> getAllEncadrants() {
        return encadrantRepository.findAll();
    }
    public List<Encadrant> getAllEncadrantsSortedByName() {
        
        return encadrantRepository.findAll(Sort.by("name"));
    }

    public List<Encadrant> getAllEncadrantsSortedByFilier() {
        
        return encadrantRepository.findAll(Sort.by("filier"));
    }


    public Encadrant getEncadrantById(long id) {
        return encadrantRepository.findById(id);
    }

    public List<Encadrant> getEncadrantsByName(String name) {
        return encadrantRepository.findByName(name);
    }

    public  List<Encadrant>  getEncadrantsByLastname(String lastname) {
        return encadrantRepository.findByLastname(lastname);
    }

    public Encadrant getEncadrantByEmail(String email) {
        return encadrantRepository.findByEmail(email);
    }

    public  List<Encadrant>  getEncadrantsByValidation(Boolean validation) {
        return encadrantRepository.findByValidation(validation);
    }

    public List<Encadrant> getEncadrantsByFilier(String filier) {
        return encadrantRepository.findByFilier(filier);
    }

    public List<Encadrant> getEncadrantsByGroupesNumber(int groupesNumber) {
        return encadrantRepository.findByGroupesnumber(groupesNumber);
    }
    public List<Encadrant> getEncadrantByImage(String image) {
         return encadrantRepository.findByImage(image);   
    }

    public List<Encadrant> getEncadrantByFilierAndGroupesnumberLessThanEqual(String filiere, int maxGroupes){
        return encadrantRepository.findByFilierAndGroupesnumberLessThanEqual(filiere, maxGroupes);
    }
    
    //update methods EncadrantServices

    public Encadrant updateEncadrantGroupesNumber(long id, int newGroupesNumber) {
        Encadrant encadrant = encadrantRepository.findById(id);
        encadrant.setGroupesnumber(newGroupesNumber);
        return encadrantRepository.save(encadrant);
    }

    public Encadrant updateEncadrantPassword(long id, String password) {
        Encadrant encadrant = encadrantRepository.findById(id);
        encadrant.setPassword(password);
        return encadrantRepository.save(encadrant);
    }

    public Encadrant updateEncadrantFilier(long id, String filier) {
        Encadrant encadrant = encadrantRepository.findById(id);
        encadrant.setFilier(filier);
        return encadrantRepository.save(encadrant);
    }



    public Encadrant updateEncadrant(long id, Encadrant updatedEncadrant) {
        Encadrant encadrant = encadrantRepository.findById(id);
        if (encadrant == null) {
            // Handle error: Encadrant with given ID not found
            return null;
        }
        
        // Update properties of Encadrant with values from updatedEncadrant
        
        encadrant.setName(updatedEncadrant.getName());
        encadrant.setLastname(updatedEncadrant.getLastname());
        encadrant.setEmail(updatedEncadrant.getEmail());
        encadrant.setPassword(updatedEncadrant.getPassword());
        encadrant.setValidation(updatedEncadrant.isValidation());
        encadrant.setFilier(updatedEncadrant.getFilier());
        encadrant.setGroupesnumber(updatedEncadrant.getGroupesnumber());
        encadrant.setImage(updatedEncadrant.getImage());
        
        return encadrantRepository.save(encadrant);
    }

    public Encadrant updateEncadrantInfo (long id, Encadrant updatedEncadrant) {
        Encadrant encadrant = encadrantRepository.findById(id);
        if (encadrant == null) {
            // Handle error: Encadrant with given ID not found
            return null;
        }
        
        // Update properties of Encadrant with values from updatedEncadrant
        
        encadrant.setName(updatedEncadrant.getName());
        encadrant.setLastname(updatedEncadrant.getLastname());
        encadrant.setEmail(updatedEncadrant.getEmail());
        
       
        
        return encadrantRepository.save(encadrant);
    }

    public Encadrant updateEncadrantImage(long id, String image) {
        Encadrant encadrant = encadrantRepository.findById(id);
        encadrant.setImage(image);
        return encadrantRepository.save(encadrant);
    }

    //delete method EncadrantServices


    public boolean deleteEncadrant(long id) {
        if (encadrantRepository.existsById(id)) {
            encadrantRepository.deleteById(id);
            return true;
        } else {
            // Handle error: Etudiant with given ID not found
            return false;
        }
    }

     //create new Encadrant

     public void save(Encadrant encadrant){
        encadrantRepository.save(encadrant);
    }

    //check if existe
    public boolean ExisteById(long id){
        return encadrantRepository.existsById(id);
    }
    public boolean ExisteByEmail(String email){
        return encadrantRepository.existsByEmail(email);
    }

    // //check the validation
    // public List<Encadrant> getValidatedEncadrants(){
    //    return encadrantRepository.isValidated();

    // }

}
