package com.gestionpfes.adnan.Controllers.Contacter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gestionpfes.adnan.models.Request;
import com.gestionpfes.adnan.services.RequestService;

import jakarta.servlet.http.HttpSession;

@Controller
public class EtudiantContacterControlle {

    @Autowired
private RequestService requestService;






@PostMapping("/Etudiant/contacter")
public String contacterusers( @RequestParam("ID") Long ID,
                             @RequestParam(required = false) String report,
                             @RequestParam("sujet") String sujet,
                             RedirectAttributes re, 
                                HttpSession session ){

    Long userid = (Long) session.getAttribute("userID");
    if(report != null){

        Request requestrepoRequest = new Request();
   
        requestrepoRequest.setSeen(false);
        requestrepoRequest.setStatus(sujet);
        requestrepoRequest.setSubject(report );
        requestrepoRequest.setUserSenderId(userid);
        requestrepoRequest.setUserGeterId(ID);

        requestService.createRequest(requestrepoRequest);
         

        re.addFlashAttribute("messagesucces", "Votre message a été envoyé");
        return "redirect:/Encadrant/Visiter/"+ID;
                    }else{

                        re.addFlashAttribute("messagesucces", "SVP remplir le formulaire correctement");
                        return "redirect:/Encadrant/Visiter/"+ID;
                    }




                                }
    
}
