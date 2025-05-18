package com.gestionpfes.adnan.Controllers.profilesControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.gestionpfes.adnan.models.Admin;
import com.gestionpfes.adnan.models.Encadrant;
import com.gestionpfes.adnan.models.Etudiant;
import com.gestionpfes.adnan.models.User;
import com.gestionpfes.adnan.services.AdminService;
import com.gestionpfes.adnan.services.EncadrantService;
import com.gestionpfes.adnan.services.EtudiantService;
import com.gestionpfes.adnan.services.UserService;



@Controller
public class ProfileEtudiantVisiterController {

    @Autowired
    AdminService adminservice ;
     
    @Autowired
    EncadrantService encadrantService;

    @Autowired
    EtudiantService etudiantService;

    @Autowired
    UserService userService;




    @GetMapping("/Etudiant/Visiter/{visitId}")

    public String adminVisiter(@PathVariable() Long visitId , Model model ){

         User user =  userService.getUserById(visitId);

         if(user.getRole().equals("admin")){
            Admin admin = adminservice.getAdminById(visitId);
            model.addAttribute("userdata", admin);
            model.addAttribute("filier", null);
            model.addAttribute("napoge", null);
            return "profileVisiteEtudiant" ;
         } else if(user.getRole().equals("encadrant")){
            Encadrant encadrant = encadrantService.getEncadrantById(visitId);
            model.addAttribute("userdata", encadrant);

            model.addAttribute("filier", encadrant.getFilier());
            model.addAttribute("napoge", null);
            return "profileVisiteEtudiant" ;
         }else {
            Etudiant etudiant = etudiantService.getEtudiantById(visitId);
            model.addAttribute("userdata", etudiant);
            model.addAttribute("filier", etudiant.getFilier());
            model.addAttribute("napoge", etudiant.getNapoge());
            return "profileVisiteEtudiant" ;
         }
         
    }
    
}
