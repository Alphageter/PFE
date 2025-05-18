package com.gestionpfes.adnan.Controllers.gestiongroupeEtudiant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gestionpfes.adnan.models.Etudiant;
import com.gestionpfes.adnan.models.Groupe;
import com.gestionpfes.adnan.models.Request;
import com.gestionpfes.adnan.services.EtudiantService;
import com.gestionpfes.adnan.services.GroupeService;
import com.gestionpfes.adnan.services.RequestService;


import jakarta.servlet.http.HttpSession;

@Controller
public class DemandeReservationcontroller {
    
    @Autowired
    private RequestService requestService ;

    @Autowired
    private GroupeService groupeService;

    @Autowired
    private EtudiantService etudiantService;


    @GetMapping("/Etudiant/demandAutorisation")
    public String askforbooking( Model model  , HttpSession session , RedirectAttributes re){

        Long userid = (Long) session.getAttribute("userID");
        Etudiant etudiantsender = etudiantService.getEtudiantById(userid);
        Groupe groupe = groupeService.findById(etudiantsender.getGroupeID()).orElse(null);
        if(groupe != null)
        {
        Request requestreserver = new Request();
        requestreserver.setSeen(false);
        requestreserver.setStatus("reservation");
        requestreserver.setSubject("Mr."+etudiantsender.getLastname() +" demander l'autorisation de réserver un rendez-vous pour le groupe "+groupe.getName() );
        requestreserver.setUserSenderId(etudiantsender.getId());
        requestreserver.setUserGeterId(groupe.getEndarantID());

   
        requestService.createRequest(requestreserver);

        re.addFlashAttribute("messagesucces", "la demande a été envoyée avec succès");
                  
        return "redirect:/Etudiant/Groupe";
    }
    else{

        re.addFlashAttribute("messagfail", "il ya des problem pour trouver votre groupe");
                  
        return "redirect:/Etudiant/Groupe";

    }

    }







}
