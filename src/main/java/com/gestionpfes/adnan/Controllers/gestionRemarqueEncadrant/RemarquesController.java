package com.gestionpfes.adnan.Controllers.gestionRemarqueEncadrant;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gestionpfes.adnan.models.Etudiant;
import com.gestionpfes.adnan.models.Groupe;
import com.gestionpfes.adnan.models.Request;
import com.gestionpfes.adnan.services.EtudiantService;
import com.gestionpfes.adnan.services.GroupeService;
import com.gestionpfes.adnan.services.RequestService;

import jakarta.servlet.http.HttpSession;

@Controller
public class RemarquesController {


     @Autowired
     private EtudiantService etudiantService;

     @Autowired
     private GroupeService groupeService;

     @Autowired
     private RequestService requestService;



     @GetMapping("/Encadrant/remarque/{id}")
     public String displaynewremarque(@PathVariable("id") Long id ,Model model ){

        
        model.addAttribute("groupeid", id);
        return "RemarqueEncadrant";
     }

    @PostMapping("/Encadrant/saveremarque")
    public String sendRemarque( @RequestParam("groupeid") Long groupeid  ,
                                @RequestParam(required = false) String remarque  , 
                                HttpSession session , RedirectAttributes re  ,Model model  ){

                                    for(int i=0;i<7;i++){
                                        System.out.println("the groupeid given is"+groupeid);
                                    }
              Groupe groupe = groupeService.findById(groupeid).orElse(null);

              if(groupe!= null){

                Long userid = (Long) session.getAttribute("userID");
                  List<Etudiant> listetudiants = etudiantService.getEtudiantByGroupeID(groupeid);
                  if(!listetudiants.isEmpty()){
                  for(Etudiant etudiant : listetudiants){
                     
                    Request requestremarque = new Request();
                    requestremarque.setSeen(false);
                    requestremarque.setStatus("remarque");
                    requestremarque.setSubject(remarque);
                    requestremarque.setUserSenderId(userid);
                    requestremarque.setUserGeterId(etudiant.getId());

                    requestService.createRequest(requestremarque);

                  }

                  re.addFlashAttribute("messagesucces", "Votre message a été envoyé avec succès ");
                    return "redirect:/Encadrant/moreinfoGroupe/"+groupeid;
                }else{
                    model.addAttribute("messagfail", "il y a un problème pour trouver les "+
                    "members de le groupe cible");
                return "RemarqueEncadrant";

                }

              }else{

                model.addAttribute("messagfail", "il y a un problème pour trouver le groupe cible");
                return "RemarqueEncadrant";
            }


                                }



    
}
