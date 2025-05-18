package com.gestionpfes.adnan.Controllers.gestionuserscontrollers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gestionpfes.adnan.handleErrores.EtudiantNotFoundException;
import com.gestionpfes.adnan.models.Etudiant;
import com.gestionpfes.adnan.services.EtudiantService;


import org.springframework.ui.Model;


@Controller


public class ComptesEtudiantController {
    

    @Autowired 
    private EtudiantService service;
     
     @GetMapping("/Admin/listEtudiant")
     public String getUsersPage(Model model)
     
     {
          List<Etudiant> listetudiant = service.getAllEtudiantsSortedByFilier();
          model.addAttribute("listetudiant", listetudiant);
         return "listEtudiant";
     }
    
     @GetMapping("/Admin/newEtudiant")
     public String showNewForm(Model model) {
         model.addAttribute("etudiant", new Etudiant());
         model.addAttribute("pageTitle", "Ajouter nouvel Etudiant");
         return "newEtudiant";
     }
     @PostMapping("/Admin/saveEtudiant")
     public String saveUser(Etudiant  etudiant, Model model,RedirectAttributes ra) {
        if(service.ExisteByEmail(etudiant.getEmail()) || service.ExisteByNapoge(etudiant.getNapoge())){
            for(int i = 0 ; i<7;i++){
                System.out.println("but they are the sameeeee !!!!!");
            }
            model.addAttribute("messagfail", "l'utilisateur a déjà été enregistré.");
           model.addAttribute("pageTitle", "Ajouter nouvel Etudiant");
            return "newEtudiant";    
        }
        else{

            for(int i = 0 ; i<7;i++){
                System.out.println("but they are WHY YOU JUMPP !!!!!");
            }
         service.save(etudiant);
         ra.addFlashAttribute("messagesucces", "L'utilisateur a été enregistré avec succès.");
         return "redirect:/Admin/listEtudiant";
        }
     }
 
     @GetMapping("/Admin/editEtudiant/{id}")
     public String showEditForm(@PathVariable("id") Long id, Model model, RedirectAttributes ra) {
         try {
            Etudiant etudiant = service.getEtudiantById(id);
            
             model.addAttribute("etudiant", etudiant);
             model.addAttribute("pageTitle", "Edit User :"+etudiant.getName()+" "+etudiant.getLastname()+
             "(ID: " + id + ")");
             
             return "updateEtudiant";
         } catch (EtudiantNotFoundException e) {
             ra.addFlashAttribute("messagfail", e.getMessage());
             return "redirect:/Admin/listEtudiant";
         }
     }
 
     @PostMapping("/Admin/editEtudiant")
     public String editUser(Etudiant  updateEtudiant, RedirectAttributes ra) {
         service.updateEtudiant(updateEtudiant.getId(), updateEtudiant);
         ra.addFlashAttribute("messagesucces", "L'utilisateur a été mis à jour avec succès.");
         return "redirect:/Admin/listEtudiant";
     }
 
     @GetMapping("/Admin/deleteEtudiant/{id}")
     public String deleteUser(@PathVariable("id") Long id, RedirectAttributes ra) {
         try {
             service.deleteEtudiant(id);
             ra.addFlashAttribute("messagesucces", "L'utilisateur  ID " + id + " a été supprimé..");
         } catch (EtudiantNotFoundException e) {
             ra.addFlashAttribute("messagefail", e.getMessage());
         }
         return "redirect:/Admin/listEtudiant";
     }
   

@PostMapping("/Admin/searchEtudiant")
public String searchAdmin(@RequestParam("search") String searchQuery, Model model,
@RequestParam(value = "validation",required = false) Boolean validation,
 RedirectAttributes redirectAttributes) {
    String[] searchTerms = searchQuery.split("\\s+");

    List<Etudiant> matchingEtudiants = new ArrayList<>();
    
    for (String term : searchTerms) {
        try {
           
            long id = Long.parseLong(term);
            Etudiant etudiantByid = service.getEtudiantById(id);
            if (etudiantByid != null) {
                if (validation == null || etudiantByid.isValidation() == validation.booleanValue()) {
                matchingEtudiants.add(etudiantByid);
                }
                continue;
            }
            Long napoge = Long.parseLong(term);
             Etudiant etudiantbyNapoge = service.getEtudiantByN_apoge(napoge);
             if (etudiantbyNapoge != null) {
                if (validation == null || etudiantbyNapoge.isValidation() == validation.booleanValue()) {
                matchingEtudiants.add(etudiantbyNapoge);
                }
                continue;
            }



        } catch (NumberFormatException e) {}

        List<Etudiant> etudiantsByName =  service.getEtudiantsByName(term);
       
        if (!etudiantsByName.isEmpty()) {
            for (Etudiant etudiant : etudiantsByName) {
                if (validation == null || etudiant.isValidation() == validation.booleanValue()) {
            matchingEtudiants.add(etudiant);
                }
            }
            continue;
        }     
        List<Etudiant> etudiantsByLastname =  service.getEtudiantsByLastname(term);
        if (!etudiantsByLastname.isEmpty()) {
            for (Etudiant etudiant : etudiantsByLastname) {
                if (validation == null || etudiant.isValidation() == validation.booleanValue()) {
            matchingEtudiants.add(etudiant);
                }
            }
            continue;
        }
     
        // Check if the term is a valid email using a regular expression
        if (term.contains("@")) { 
            Etudiant etudiantByEmail = service.getEtudiantByEmail(term); 
            if (etudiantByEmail != null) {
                if (validation == null || etudiantByEmail.isValidation() == validation.booleanValue()) {
                matchingEtudiants.add(etudiantByEmail);
                }
                continue;
            }
        }        
        List<Etudiant> etudiantsByFilier =  service.getEtudiantsByFilier(term);
        if (!etudiantsByFilier.isEmpty()) {
            for (Etudiant etudiant : etudiantsByFilier) {
                if (validation == null || etudiant.isValidation() == validation.booleanValue()) {
            matchingEtudiants.add(etudiant);
                }
            }
            continue;
        }           
    }

    if (!matchingEtudiants.isEmpty()) {
        Set<Etudiant> uniqueEtudiants = new HashSet<>(matchingEtudiants);
        matchingEtudiants.clear();
        matchingEtudiants.addAll(uniqueEtudiants);
        model.addAttribute("listetudiant", matchingEtudiants);
        return "listEtudiant";
    } else {
        redirectAttributes.addFlashAttribute("messagfail", "L'utilisateur n'existe pas."); 
         return "redirect:/Admin/listEtudiant"; 
    }
}
}
