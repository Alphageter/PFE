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

import com.gestionpfes.adnan.handleErrores.EncadrantNotFoundException;
import com.gestionpfes.adnan.models.Encadrant;
import com.gestionpfes.adnan.services.EncadrantService;


import org.springframework.ui.Model;


@Controller
public class ComptesEncadrantController {
    @Autowired 
    private EncadrantService service;

    
     @GetMapping("/Admin/listEncadrant")
     public String getUsersPage(Model model)
     
     {
          List<Encadrant> listencadrant = service.getAllEncadrantsSortedByFilier();
          model.addAttribute("listencadrant", listencadrant);
         return "listEncadrant";
     }
    
     @GetMapping("/Admin/newEncadrant")
     public String showNewForm(Model model) {
         model.addAttribute("encadrant", new Encadrant());
         model.addAttribute("pageTitle", "Ajouter nouvel Encadrant");
         return "newEncadrant";
     }
     @PostMapping("/Admin/saveEncadrant")
     public String saveUser(Encadrant  encadrant, Model model,RedirectAttributes ra) {
        if(service.ExisteByEmail(encadrant.getEmail())){
           model.addAttribute("messagfail", "l'utilisateur a déjà été enregistré.");
           model.addAttribute("pageTitle", "Ajouter nouvel Encadrant");
            return "newEncadrant";    
        }
        else{
         service.save(encadrant);
         ra.addFlashAttribute("messagesucces", "L'utilisateur a été enregistré avec succès.");
         return "redirect:/Admin/listEncadrant";
        }
     }
 
     @GetMapping("/Admin/editEncadrant/{id}")
     public String showEditForm(@PathVariable("id") Long id, Model model, RedirectAttributes ra) {
         try {
            Encadrant encadrant = service.getEncadrantById(id);
            
             model.addAttribute("encadrant", encadrant);
             model.addAttribute("pageTitle", "Edit User :"+encadrant.getName()+" "+encadrant.getLastname()+
             "(ID: " + id + ")");
             
             return "updateEncadrant";
         } catch (EncadrantNotFoundException e) {
             ra.addFlashAttribute("messagfail", e.getMessage());
             return "redirect:/Admin/listEncadrant";
         }
     }
 
     @PostMapping("/Admin/editEncadrant")
     public String editUser(Encadrant  updateEncadrant, RedirectAttributes ra) {
         service.updateEncadrant(updateEncadrant.getId(), updateEncadrant);
         ra.addFlashAttribute("messagesucces", "L'utilisateur a été mis à jour avec succès.");
         return "redirect:/Admin/listEncadrant";
     }
 
     @GetMapping("/Admin/deleteEncadrant/{id}")
     public String deleteUser(@PathVariable("id") Long id, RedirectAttributes ra) {
         try {
             service.deleteEncadrant(id);
             ra.addFlashAttribute("messagesucces", "L'utilisateur  ID " + id + " a été supprimé..");
         } catch (EncadrantNotFoundException e) {
             ra.addFlashAttribute("messagefail", e.getMessage());
         }
         return "redirect:/Admin/listEncadrant";
     }
   


@PostMapping("/Admin/searchEncadrant")
public String searchEncadrant(@RequestParam("search") String searchQuery, 
                              @RequestParam(value = "validation", required = false) Boolean validation,
                              Model model, RedirectAttributes redirectAttributes) {
    String[] searchTerms = searchQuery.split("\\s+");

    List<Encadrant> matchingEncadrants = new ArrayList<>();

    for (String term : searchTerms) {
        try {
            long id = Long.parseLong(term);
            Encadrant encadrant = service.getEncadrantById(id);
            if (encadrant != null) {
                if (validation == null || encadrant.isValidation() == validation.booleanValue()) {
                    matchingEncadrants.add(encadrant);
                }
                continue;
            }
        } catch (NumberFormatException e) {}

        List<Encadrant> encadrantsByName =  service.getEncadrantsByName(term);
        if (!encadrantsByName.isEmpty()) {
            for (Encadrant encadrant : encadrantsByName) {
                if (validation == null || encadrant.isValidation() == validation.booleanValue()) {
                    matchingEncadrants.add(encadrant);
                }
            }
            continue;
        }
        List<Encadrant> encadrantsByLastname =  service.getEncadrantsByLastname(term);
        if (!encadrantsByLastname.isEmpty()) {
            for (Encadrant encadrant : encadrantsByLastname) {
                if (validation == null || encadrant.isValidation() == validation.booleanValue()) {
                    matchingEncadrants.add(encadrant);
                }
            }
            continue;
        }

        // Check if the term is a valid email using a regular expression
        if (term.contains("@")) { 
            Encadrant encadrantByEmail = service.getEncadrantByEmail(term); 
            if (encadrantByEmail != null) {
                if (validation == null || encadrantByEmail.isValidation() == validation.booleanValue()) {
                    matchingEncadrants.add(encadrantByEmail);
                }
                continue;
            }
        }          
        List<Encadrant> encadrantsByFilier =  service.getEncadrantsByFilier(term);
        if (!encadrantsByFilier.isEmpty()) {
            for (Encadrant encadrant : encadrantsByFilier) {
                if (validation == null || encadrant.isValidation() == validation.booleanValue()) {
                    matchingEncadrants.add(encadrant);
                }
            }
            continue;
        }
    }

    if (!matchingEncadrants.isEmpty()) {
        Set<Encadrant> uniqueEncadrants = new HashSet<>(matchingEncadrants);
        matchingEncadrants.clear();
        matchingEncadrants.addAll(uniqueEncadrants);
        model.addAttribute("listencadrant", matchingEncadrants);
        return "listEncadrant";
    } else {
        redirectAttributes.addFlashAttribute("messagfail", "L'utilisateur n'existe pas."); 
         return "redirect:/Admin/listEncadrant"; 
    }
}

    
}
