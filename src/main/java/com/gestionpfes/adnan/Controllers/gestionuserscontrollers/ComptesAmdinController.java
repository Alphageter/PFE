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

import com.gestionpfes.adnan.handleErrores.AdminNotFoundException;
import com.gestionpfes.adnan.models.Admin;
import com.gestionpfes.adnan.services.AdminService;


import org.springframework.ui.Model;



@Controller

public class ComptesAmdinController {
    
    @Autowired 
    private AdminService service;
     
     @GetMapping("/Admin/listAdmin")
     public String getUsersPage(Model model)
     
     {
          List<Admin> listAdmins = service.getAllAdminsSortedByName();
          model.addAttribute("listadmins", listAdmins);
         return "listAdmin";
     }
    
     @GetMapping("/Admin/newAdmin")
     public String showNewForm(Model model) {
         model.addAttribute("admin", new Admin());
         model.addAttribute("pageTitle", "Ajouter nouvel admin");
         return "newAdmin";
     }
     @PostMapping("/Admin/saveAdmin")
     public String saveUser(Admin  admin, Model model,RedirectAttributes ra) {
        if(service.ExisteByEmail(admin.getEmail())){
            
           model.addAttribute("messagfail", "l'utilisateur a déjà été enregistré.");
           model.addAttribute("pageTitle", "Ajouter nouvel admin");
            return "newAdmin";    
        }
        else{
         service.save(admin);
         ra.addFlashAttribute("messagesucces", "L'utilisateur a été enregistré avec succès.");
         return "redirect:/Admin/listAdmin";
        }
     }
 
     @GetMapping("/Admin/editAdmin/{id}")
     public String showEditForm(@PathVariable("id") Long id, Model model, RedirectAttributes ra) {
         try {
             Admin admin = service.getAdminById(id);
           
             model.addAttribute("admin", admin);
             model.addAttribute("pageTitle", "Edit User :"+admin.getName()+" "+admin.getLastname()+
             "(ID: " + id + ")");
             
             return "updateAdmin";
         } catch (AdminNotFoundException e) {
             ra.addFlashAttribute("messagfail", e.getMessage());
             return "redirect:/Admin/listAdmin";
         }
     }
 
     @PostMapping("/Admin/editAdmin")
     public String editUser(Admin  updateadmin, RedirectAttributes ra) {
         service.updateAdmin(updateadmin.getId(), updateadmin);
         ra.addFlashAttribute("messagesucces", "L'utilisateur a été mis à jour avec succès.");
         return "redirect:/Admin/listAdmin";
     }
 
     @GetMapping("/Admin/deleteAdmin/{id}")
     public String deleteUser(@PathVariable("id") Long id, RedirectAttributes ra) {
         try {
             service.deleteAdmin(id);
             ra.addFlashAttribute("messagesucces", "L'utilisateur  ID " + id + " a été supprimé..");
         } catch (AdminNotFoundException e) {
             ra.addFlashAttribute("messagefail", e.getMessage());
         }
         return "redirect:/Admin/listAdmin";
     }
   

@PostMapping("/Admin/searchAdmin")
public String searchAdmin(@RequestParam("search") String searchQuery, Model model,
@RequestParam(value = "validation",required = false) Boolean validation,
 RedirectAttributes redirectAttributes) {
    
    String[] searchTerms = searchQuery.split("\\s+");

    List<Admin> matchingAdmins = new ArrayList<>();

    for (String term : searchTerms) {
        try {
            long id = Long.parseLong(term);
            Admin admin = service.getAdminById(id);
            if (validation == null || admin.isValidation() == validation.booleanValue()) {
                matchingAdmins.add(admin);
                continue;
            }
        } catch (NumberFormatException e) {}

        List<Admin> adminsByName =  service.getAdminByName(term);
        if (!adminsByName.isEmpty()) {

          for (Admin admin : adminsByName) {
            if (validation == null || admin.isValidation() == validation.booleanValue()) {
            matchingAdmins.add(admin);
            }
        }
        
            continue;
        }
        
        List<Admin> adminsByLastname =  service.getAdminByLastname(term);
        if (!adminsByLastname.isEmpty()) {
            for (Admin admin : adminsByLastname) {
                if (validation == null || admin.isValidation() == validation.booleanValue()) {
            matchingAdmins.add(admin);
                }
            }
            continue;
        }

        // Check if the term is a valid email using a regular expression
        if (term.contains("@")) {           
            Admin adminByEmail = service.getAdminByEmail(term);  
            if (adminByEmail != null) {               
                    if (validation == null || adminByEmail.isValidation() == validation.booleanValue()) {
                matchingAdmins.add(adminByEmail);
                    }
                
                continue;
            }
        }
    }
    if (!matchingAdmins.isEmpty()) {
        Set<Admin> uniqueAdmins = new HashSet<>(matchingAdmins);
      matchingAdmins.clear();
       matchingAdmins.addAll(uniqueAdmins);
        model.addAttribute("listadmins", matchingAdmins);
        return "listAdmin";
    } else {
        redirectAttributes.addFlashAttribute("messagfail", "L'utilisateur n'existe pas."); 
         return "redirect:/Admin/listAdmin"; 
    }
}
 



   }
