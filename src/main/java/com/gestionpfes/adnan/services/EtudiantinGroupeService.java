package com.gestionpfes.adnan.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.gestionpfes.adnan.Repository.EtudiantinGroupeRepository;
import com.gestionpfes.adnan.handleErrores.handleErrortest.EntityNotFoundException;
import com.gestionpfes.adnan.models.EtudiantinGroupe;

@Transactional
@Service
public class EtudiantinGroupeService {

    @Autowired
    private EtudiantinGroupeRepository etudiantinGroupeRepository;

    //get methods

    public List<EtudiantinGroupe> getAlletudiantinGroupes() {
        return etudiantinGroupeRepository.findAll();
    }

    public List<EtudiantinGroupe> getAlletudiantinGroupesSorted(Sort sort) {
        return etudiantinGroupeRepository.findAll(sort);
    }

    public EtudiantinGroupe getetudiantinGroupeById(Long id) {
        return etudiantinGroupeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("etudiantinGroupe not found with id " + id));
    }

    public EtudiantinGroupe getetudiantinGroupesByetudiantID(Long etudiantID) {
        return etudiantinGroupeRepository.findByEtudiantID(etudiantID);
    }

    public List<EtudiantinGroupe> getetudiantinGroupesByGroupeID(Long groupeID) {
        return etudiantinGroupeRepository.findByGroupeID(groupeID);
    }

    //create method

    public EtudiantinGroupe createetudiantinGroupe(EtudiantinGroupe etudiantinGroupe) {
        return etudiantinGroupeRepository.save(etudiantinGroupe);
    }
    
    //UPDATE methods
    public EtudiantinGroupe updateetudiantinGroupe(Long id ,EtudiantinGroupe etudiantinGroupe) {
        EtudiantinGroupe etudiantinGroupe2 = etudiantinGroupeRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("etudiantinGroupe not found with id " + id));
        if(etudiantinGroupe2==null){
            return null ;
        }
        etudiantinGroupe2.setEtudiantID(etudiantinGroupe.getEtudiantID());
        etudiantinGroupe2.setGroupeID(etudiantinGroupe.getGroupeID());

        return etudiantinGroupeRepository.save(etudiantinGroupe2);
    }

    //DELTE method
    public void deleteetudiantinGroupeById(Long id) {
        etudiantinGroupeRepository.deleteById(id);
    }

    public void deleteEtudiantinGroupeByEtudiantID(Long etudiantID){
        etudiantinGroupeRepository.deleteByEtudiantID(etudiantID);
    }
    
    public void deleteEtudiantinGroupeByGroupID(Long groupeID){
        etudiantinGroupeRepository.deleteByGroupeID(groupeID);
    }

    public boolean existsetudiantinGroupeByEtudiantID(Long etudiantID) {
        return etudiantinGroupeRepository.existsByEtudiantID(etudiantID);
    }
    
}
