package com.gestionpfes.adnan.Controllers.gestiongroupesEncadrantControllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import com.gestionpfes.adnan.models.Groupe;

import com.gestionpfes.adnan.services.GroupeService;

import jakarta.servlet.http.HttpSession;

@Controller
public class SearchGroupeEncadrant {





    @Autowired
private GroupeService groupeService ;


    @PostMapping("/Encadrant/searchGroupe")
public String searchAdmin(@RequestParam("search") String searchQuery, Model model,

                                          RedirectAttributes redirectAttributes 
                                          , HttpSession session) {

    Long userid = (Long) session.getAttribute("userID")   ;                                     
    
    String[] searchTerms = searchQuery.split("\\s{2,}");

    List<Groupe> matchinggroupes= new ArrayList<>();

    for (String term : searchTerms) {
        try {
            Groupe groupesearch = null;
            long id = Long.parseLong(term);
            Optional<Groupe> optionalgroupe = groupeService.findById(id);
            if(optionalgroupe.isPresent()) {
            groupesearch = optionalgroupe.get();
            }
           if(groupesearch!=null && groupesearch.getEndarantID().equals(userid) ){
            
            matchinggroupes.add(groupesearch);
                continue;
            }
            
        } catch (NumberFormatException e) {}

        Groupe groupesByName =  groupeService.findByName(term);
        if (groupesByName != null && groupesByName.getEndarantID().equals(userid) ) {
            matchinggroupes.add(groupesByName);      
        
            continue;
        }
        
        
        List<Groupe> groupesByFilier =  groupeService.findByFilier(term);
        if (!groupesByFilier.isEmpty()) {
                     for( Groupe groupe : groupesByFilier ){
                     if(groupe.getEndarantID().equals(userid)){
                matchinggroupes.add(groupe);
            }
        }
            continue;
        }

        List<Groupe> groupesByStatus =  groupeService.findByStatus(term);
        if (!groupesByStatus.isEmpty()) {
           
            for( Groupe groupe : groupesByStatus ){
                if(groupe.getEndarantID().equals(userid)){
           matchinggroupes.add(groupe);
       }
   }
                
       
            continue;
        }

        List<Groupe> groupesByTypeofwork =  groupeService.findByTypeOfWork(term);
        if (!groupesByTypeofwork.isEmpty()) {
               
            for( Groupe groupe : groupesByTypeofwork ){
                if(groupe.getEndarantID().equals(userid)){
           matchinggroupes.add(groupe);
       }
   }
      
            continue;
        }


    }
    if (!matchinggroupes.isEmpty()) {
        
        Set<Groupe> uniqueGroupes = new HashSet<>(matchinggroupes);
        matchinggroupes.clear();
        matchinggroupes.addAll(uniqueGroupes);
        model.addAttribute("listgroupes", matchinggroupes);
        return "listGroupeEncadrant";
    } else {
       
        redirectAttributes.addFlashAttribute("messagfail", "Aucun groupe n'a été trouvé. Veuillez vous assurer d'insérer les données correctes."); 
         return "redirect:/Encadrant/Groupes"; 
    }
}
 
    
}
