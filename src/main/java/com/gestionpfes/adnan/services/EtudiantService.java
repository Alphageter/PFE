package com.gestionpfes.adnan.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.gestionpfes.adnan.Repository.EtudiantRepository;
import com.gestionpfes.adnan.models.Etudiant;
import com.gestionpfes.adnan.models.EtudiantinGroupe;


@Service
public class EtudiantService {

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private EtudiantinGroupeService etudiantinGroupeService;
     

    //get methods EtudiantServices

   

    public List<Etudiant> getAllEtudiants() {
        return etudiantRepository.findAll();
    }

    public List<Etudiant> getAllEtudiantsSortedByName() {
        return etudiantRepository.findAll(Sort.by("name"));
    }

    public List<Etudiant> getAllEtudiantsSortedByFilier() {
        
        return etudiantRepository.findAll(Sort.by("filier")) ;
    }
    
    public Etudiant getEtudiantById(long id) {
        return etudiantRepository.findById(id);
    }

    public List<Etudiant> getEtudiantsByName(String name) {
        return etudiantRepository.findByName(name);
    }

    public List<Etudiant> getEtudiantsByLastname(String lastname) {
        return etudiantRepository.findByLastname(lastname);
    }

    public Etudiant getEtudiantByEmail(String email) {
        return etudiantRepository.findByEmail(email);
    }

    public  List<Etudiant>  getEtudiantsByValidation(Boolean validation) {
        return etudiantRepository.findByValidation(validation);
    }



    public Etudiant getEtudiantByN_apoge(long Napoge) {
        return etudiantRepository.findByNapoge(Napoge);
    }

    public List<Etudiant> getEtudiantsByFilier(String filier) {
        return etudiantRepository.findByFilier(filier);
    }

    public List<Etudiant> getEtudiantByImage(String image) {
        return etudiantRepository.findByImage(image);   
   }

   public List<Etudiant> getEtudiantByGroupeID(Long groupeid){
       return etudiantRepository.findByGroupeID(groupeid);
   }

    
    

  
    //update methods EtudiantServices

    public Etudiant updateEtudiant(long id, Etudiant updatedEtudiant) {
        Etudiant etudiant = etudiantRepository.findById(id);
        if (etudiant == null) {
            // Handle error: Etudiant with given ID not found
            return null;
        }
        
        // Update properties of Etudiant with values from updatedEtudiant
        etudiant.setName(updatedEtudiant.getName());
        etudiant.setLastname(updatedEtudiant.getLastname());
        etudiant.setEmail(updatedEtudiant.getEmail());
        etudiant.setPassword(updatedEtudiant.getPassword());
        etudiant.setValidation(updatedEtudiant.isValidation());
        etudiant.setNapoge(updatedEtudiant.getNapoge());
        etudiant.setImage(updatedEtudiant.getImage());
        
        return etudiantRepository.save(etudiant);
    }


    public Etudiant updateEtudiantinfo(long id, Etudiant updatedEtudiant) {
        Etudiant etudiant = etudiantRepository.findById(id);
        if (etudiant == null) {
            // Handle error: Etudiant with given ID not found
            return null;
        }
        
        // Update properties of Etudiant with values from updatedEtudiant
        etudiant.setName(updatedEtudiant.getName());
        etudiant.setLastname(updatedEtudiant.getLastname());
        etudiant.setEmail(updatedEtudiant.getEmail());
       
        
        return etudiantRepository.save(etudiant);
    }
    
    public Etudiant updateEtudiantNameLastNameAndEmail(long id, String newName, String newLastName, 
    String newEmail , long newNapoge) {
        Etudiant etudiant = etudiantRepository.findById(id);
        if (etudiant == null) {
            // Handle error: Etudiant with given ID not found
            return null;
        } 
        // Update name, lastName, and email properties of Etudiant
        etudiant.setName(newName);
        etudiant.setLastname(newLastName);
        etudiant.setEmail(newEmail);
        etudiant.setNapoge(newNapoge);
        
        return etudiantRepository.save(etudiant);
    }
    
    public Etudiant updateEtudiantNapogeAndFilier(long id, long newNapoge, String newFilier) {
        Etudiant etudiant = etudiantRepository.findById(id);
        if (etudiant == null) {
            // Handle error: Etudiant with given ID not found
            return null;
        }
        
        // Update napoge and filier properties of Etudiant
        etudiant.setNapoge(newNapoge);
        etudiant.setFilier(newFilier);
        
        return etudiantRepository.save(etudiant);
    }

    public Etudiant updateEtudiantPassword(long id, String password) {
        Etudiant etudiant = etudiantRepository.findById(id);
        etudiant.setPassword(password);
        return etudiantRepository.save(etudiant);
    }    

    public Etudiant updateEtudiantImage(long id, String image) {
        Etudiant etudiant = etudiantRepository.findById(id);
        etudiant.setImage(image);
        return etudiantRepository.save(etudiant);
    }
    public Etudiant updateEtudiantGroupeID(long id, Long groupeid) {
        Etudiant etudiant = etudiantRepository.findById(id);
        etudiant.setGroupeID(groupeid);
        return etudiantRepository.save(etudiant);
    }

    //delete methods EtudiantServices
    public boolean deleteEtudiant(long id) {
        if (etudiantRepository.existsById(id)) {
            etudiantRepository.deleteById(id);
            return true;
        } else {
            // Handle error: Etudiant with given ID not found
            return false;
        }
    }
     //create new Etudiant

     public void save(Etudiant etudiant){
        etudiantRepository.save(etudiant);
    }

    //check if existe
    public boolean ExisteById(long id){
        return etudiantRepository.existsById(id);
    }
    public boolean ExisteByEmail(String email){
        return etudiantRepository.existsByEmail(email);
    }
    public boolean ExisteByNapoge (long napoge){
        return etudiantRepository.existsByNapoge(napoge);
    }

    //EXTea methodss
    public void changeEtudiantByEtudiantingroupe(Etudiant etudiantold , Etudiant etudiantnew  ){

        EtudiantinGroupe etudiantinGroupe = etudiantinGroupeService.getetudiantinGroupesByetudiantID(etudiantold.getId());
         
        etudiantinGroupe.setEtudiantID(etudiantnew.getId());
        etudiantinGroupeService.updateetudiantinGroupe(etudiantinGroupe.getId(), etudiantinGroupe);
        
        etudiantnew.setGroupeID(etudiantinGroupe.getGroupeID());
         etudiantRepository.save(etudiantnew);

         etudiantold.setGroupeID(null);
         etudiantRepository.save(etudiantold);      
    }
    

   
}
