package com.gestionpfes.adnan.Controllers.PostManTester;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestionpfes.adnan.models.EtudiantinGroupe;
import com.gestionpfes.adnan.services.EtudiantinGroupeService;



@RestController
@RequestMapping("/EG")
public class PostmanTestEG {
   
    @Autowired
    EtudiantinGroupeService etudiantinGroupeService;


    //getin list of EG by groupID

    @GetMapping("/ListByGroupeid/{groupeid}")
    public List<EtudiantinGroupe> listofEtudiantinGroupeBygroupId (@PathVariable("groupeid") Long groupeid){
      List<EtudiantinGroupe> etudiantingroupelist = new ArrayList<>();

      etudiantingroupelist = etudiantinGroupeService.getetudiantinGroupesByGroupeID(groupeid);
      return etudiantingroupelist;
    }

   //insert a value in GE

   @PostMapping("/inserte")
   public void insertinEtudiantGroupe(EtudiantinGroupe etudiantinGroupe){

    etudiantinGroupeService.createetudiantinGroupe(etudiantinGroupe);
   }


   //List of all EG

   @GetMapping(value="/lisall")
   public List<EtudiantinGroupe> getALL() {
       return etudiantinGroupeService.getAlletudiantinGroupes() ;
   }


   
    
}
