package com.gestionpfes.adnan.Controllers.gestiongroupesEncadrantControllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gestionpfes.adnan.models.Etudiant;
import com.gestionpfes.adnan.models.Groupe;
import com.gestionpfes.adnan.models.Request;
import com.gestionpfes.adnan.services.EtudiantService;
import com.gestionpfes.adnan.services.GroupeService;
import com.gestionpfes.adnan.services.RequestService;

import jakarta.servlet.http.HttpSession;

//ajouter request here authorisation donner ajouter
@Controller
public class AutorisationGroupeEncadrant {


    @Autowired
    private GroupeService groupeService;

    @Autowired
   private RequestService requestService;



   @Autowired
   private EtudiantService etudiantService;



    @GetMapping("/Encadrant/autoriserGroupe/{groupeid}")
    public String autorisationgroupe(@PathVariable("groupeid") Long groupeid ,
                                              RedirectAttributes re , HttpSession session){
                                                
        Optional<Groupe> optionalgroupe = groupeService.findById(groupeid);
        
        if(optionalgroupe.isPresent()){
                 Groupe groupe = optionalgroupe.get();
                 Long encadrantid = (Long) session.getAttribute("userId");
                List<Etudiant> listetudiant = etudiantService.getEtudiantByGroupeID(groupeid);
                for(Etudiant etudiant : listetudiant){
                                  //send request to encadrant
             
                                 Request requestajouter  = new Request();
                                 requestajouter.setSeen(false);
                                 requestajouter.setStatus("ajouter");
                                 requestajouter.setSubject("Votre Encadrant vous a donné la permission de prendre rendez-vous "); 
                                 requestajouter.setUserSenderId(encadrantid); 
                                 requestajouter.setUserGeterId(etudiant.getId());
                                 
                                 requestService.createRequest(requestajouter);
                                }

                 groupeService.updateGroupeAutorisation(groupe.getId(), true);
                 re.addFlashAttribute("messagesucces", "vous avez donné l'autorisation au groupe "+groupe.getName());
                 return "redirect:/Encadrant/Groupes";

        }else{

            re.addFlashAttribute("messagfail", "le groupe d'ID "+groupeid+" n'a pas été trouvé ");
            return "redirect:/Encadrant/Groupes";
              
        }

                                              }
    
}
