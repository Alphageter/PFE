package com.gestionpfes.adnan.Controllers.gestiongroupesEncadrantControllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.gestionpfes.adnan.models.Groupe;
import com.gestionpfes.adnan.services.GroupeService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ListGroupeEncadrant {

@Autowired
private GroupeService groupeService;

    @GetMapping("/Encadrant/Groupes")
    public String listGroupe(Model model , HttpSession session) {
        try{
          Long userID = (Long) session.getAttribute("userID");
        List<Groupe> groupes = groupeService.findByEndarantIDAndStatus(userID,"accepter");
        model.addAttribute("listgroupes", groupes);
     
    
        return "listGroupeEncadrant";
              }catch(Exception e){

           
        
            model.addAttribute("messagfail", e);
            return "profileEncadrant";

        }
    }



 



    
}
