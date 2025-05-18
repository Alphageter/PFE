package com.gestionpfes.adnan.Controllers.gestiongroupesControllers;

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
import com.gestionpfes.adnan.models.EtudiantinGroupe;
import com.gestionpfes.adnan.models.Request;
import com.gestionpfes.adnan.services.EtudiantService;
import com.gestionpfes.adnan.services.EtudiantinGroupeService;
import com.gestionpfes.adnan.services.GroupeService;
import com.gestionpfes.adnan.services.RequestService;
import jakarta.servlet.http.HttpSession;

@Controller

public class CRUDEtudiantinGroupeController {

    @Autowired
    private EtudiantService etudiantService;

    @Autowired
    private EtudiantinGroupeService etudiantinGroupesService;

    @Autowired
    private GroupeService groupeService;

    @Autowired
    private RequestService requestService;

   

//create ajouter request here

    @GetMapping("/Admin/updateGroupeEtudiants/{groupeid}")
public String updatingGroupeEtudiant(@PathVariable("groupeid") Long groupeid,
                                     Model model  , HttpSession session){
        
 List<Etudiant> listetudiants = etudiantService.getEtudiantByGroupeID(groupeid);


 model.addAttribute("listetudiants", listetudiants);
 model.addAttribute("type",2);
    
    return "updateGroupeEtudiant";
}
    
@PostMapping("/Admin/addEtudiantinGroupe")
public String addEtudiantinGroupe(   @RequestParam(required = false) Long groupeid  ,
                                     @RequestParam(required = false) String email,
                                     Model model , RedirectAttributes re,
                                     HttpSession session  ){
             Long groupeidsession = (Long) session.getAttribute("groupeid");
             Etudiant etudiant = etudiantService.getEtudiantByEmail(email);
             for(int i = 0 ; i<7 ; i++){
                System.out.println("im at the tope of add an etudiant the groupe is is"+groupeidsession+" / etudiant id"+etudiant.getId());
            }
             if(etudiant==null){
                for(int i = 0 ; i<7 ; i++){
                    System.out.println("Check 1 !!!!!!!!!!");
                }

                model.addAttribute("messagfail", "étudiant que vous insérez n'a pas été trouvé");
                model.addAttribute("type",1);
                return "updateGroupeEtudiant";
             }else{

                for(int i = 0 ; i<7 ; i++){
                    System.out.println("Check 22222222 !!!!!!!!!!");
                }
                EtudiantinGroupe etudiantinGroupe = etudiantinGroupesService.getetudiantinGroupesByetudiantID(etudiant.getId());
                if(etudiantinGroupe != null){
                          
                    for(int i = 0 ; i<7 ; i++){
                        System.out.println("Check 3 !!!!!!!!!!");
                    }
                    model.addAttribute("messagfail", "l'étudiant que vous insérez a déjà un groupe : "+etudiant.getEmail());
                    model.addAttribute("type",1);
                    return "updateGroupeEtudiant";

                }else{
                    for(int i = 0 ; i<7 ; i++){
                        System.out.println("Check 444444 !!!!!!!!!!");
                    }
                    EtudiantinGroupe etudiantinGroupeadd = new EtudiantinGroupe();
                    etudiantinGroupeadd.setEtudiantID(etudiant.getId());
                    etudiantinGroupeadd.setGroupeID(groupeidsession);
                     
                    etudiantinGroupesService.createetudiantinGroupe(etudiantinGroupeadd);
                    for(int i = 0 ; i<7 ; i++){
                        System.out.println("im at the end of add an etudiant the groupe is ");
                    }
                    
                    groupeService.updateGroupeTypeOfWork(groupeidsession, "in Group");
                    

                    etudiantService.updateEtudiantGroupeID(etudiant.getId(), groupeidsession);

                    Long adminid = (Long) session.getAttribute("userID");

                    Request requestajouter  = new Request();
                    requestajouter.setSeen(false);
                    requestajouter.setStatus("ajouter");
                    requestajouter.setSubject("vous avez été ajouté au groupe de PFE"); 
                    requestajouter.setUserSenderId(adminid); 
                    requestajouter.setUserGeterId(etudiant.getId());
                    
                    requestService.createRequest(requestajouter);

                   

                    re.addFlashAttribute("messagesucces", "l'étudiant a été rejoint avec succès");
                    re.addFlashAttribute("type",1);
                    return "redirect:/Admin/editeGroupe/"+groupeidsession;
                }
            }

                                     }


             @PostMapping("/Admin/deleteEtudiantinGroupe/")
             public String deleteEtudiantinGroupe(@RequestParam(required = false) Long etudiantID
                                           , RedirectAttributes re 
                                           , HttpSession session   
                                           , Model model   ){
                         Long groupeid = (Long) session.getAttribute("groupeid");
                         Etudiant etudiantdelete = etudiantService.getEtudiantById(etudiantID);
                       if(etudiantID!=null){
                             
                        EtudiantinGroupe etudiantinGroupe = etudiantinGroupesService.getetudiantinGroupesByetudiantID(etudiantdelete.getId());

                        etudiantinGroupesService.deleteetudiantinGroupeById(etudiantinGroupe.getId());
                        
                        etudiantService.updateEtudiantGroupeID(etudiantdelete.getId(), null);
                       
                        re.addFlashAttribute("messagesucces", "le candidat a été éliminé de ce groupe");
                       
                        groupeService.updateGroupeTypeOfWork(groupeid, "individual");

                         return "redirect:/Admin/editeGroupe/"+groupeid;

                       }else{
                        List<Etudiant> listetudiants = etudiantService.getEtudiantByGroupeID(groupeid);
                        model.addAttribute("listetudiants", listetudiants);
                        model.addAttribute("messagfail", "étudiant que vous voulez supprimer n'a pas été trouvé");
                        model.addAttribute("type",2);
                        
                        return "updateGroupeEtudiant";

                       }
                                   
             }

}
