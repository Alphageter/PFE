package com.gestionpfes.adnan.Controllers.gestiongroupesControllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gestionpfes.adnan.models.Etudiant;
import com.gestionpfes.adnan.models.EtudiantinGroupe;
import com.gestionpfes.adnan.models.Groupe;

import com.gestionpfes.adnan.services.EtudiantService;
import com.gestionpfes.adnan.services.EtudiantinGroupeService;
import com.gestionpfes.adnan.services.GroupeService;


import jakarta.servlet.http.HttpSession;

@Controller
public class UpdateGroupeController {


    @Autowired
    private GroupeService groupeService;


    @Autowired
    private EtudiantService etudiantService;

    @Autowired
    private EtudiantinGroupeService etudiantinGroupeService;

   
//add request heree ajouter et eleminer maybe

  @PostMapping("/Admin/editeGroupeinfo")
  public String updateGroupeinfo(  Groupe groupe,
                                    @RequestParam (required = false)int step,
                                    @RequestParam(required = false) Long groupeid  ,
                                    @RequestParam(required = false) String name  ,
                                    @RequestParam(required = false) String typeofwork  ,
                                    @RequestParam(required = false) String email1  ,
                                    @RequestParam(required = false) String email2,
                                    Model model , RedirectAttributes re
                                   ,HttpSession session)
      {
        
        Optional<Groupe> groupecheck = groupeService.findById(groupeid);
        if(groupecheck.isPresent()){
           
            Groupe groupechanging = groupecheck.get();
        //checking name
        if(! groupechanging.getName().equals(name)){
        if(groupeService.ExisteByName(name)){
          
            re.addFlashAttribute("messagfail", "Un groupe avec le même nom existe déjà"); 
            re.addFlashAttribute("step"  ,1);   
            return "redirect:/Admin/editeGroupe/"+groupeid;     
        
        }else{
             groupeService.updateGroupeName(groupeid, name);

        }
    }

        //Checking the new gmails 
        List<Etudiant> etudiants = etudiantService.getEtudiantByGroupeID(groupeid);
        Etudiant etudiantcheck1 = etudiants.get(0);
        Etudiant etudiantcheck2=null;
       
        if(etudiants.size()>1){
            etudiantcheck2 = etudiants.get(1);
           
        }
        if(email1 != null){
          Etudiant etudiant1 = etudiantService.getEtudiantByEmail(email1);
          if(!etudiantcheck1.getEmail().equals(etudiant1.getEmail())){
           
            EtudiantinGroupe etudiantinGroupe1 = etudiantinGroupeService.getetudiantinGroupesByetudiantID(etudiant1.getId());
            if(etudiantinGroupe1==null){
                
            etudiantService.changeEtudiantByEtudiantingroupe(etudiantcheck1, etudiant1);
        }else{
            re.addFlashAttribute("messagfail", "l'étudiant que vous insérez a déjà un groupe : "+etudiant1.getEmail());
            re.addFlashAttribute("step"  ,1);  
            return "redirect:/Admin/editeGroupe/"+groupeid;
        }
          }
        }
          if(email2!=null){
            Etudiant etudiant2 = etudiantService.getEtudiantByEmail(email2);
            if(!etudiantcheck2.getEmail().equals(etudiant2.getEmail())){
               
                EtudiantinGroupe etudiantinGroupe2 = etudiantinGroupeService.getetudiantinGroupesByetudiantID(etudiant2.getId());
                
            if(etudiantinGroupe2==null){
                
              etudiantService.changeEtudiantByEtudiantingroupe(etudiantcheck2, etudiant2);
            }else{
                re.addFlashAttribute("messagfail", "l'étudiant que vous insérez a déjà un groupe"+etudiant2.getEmail());
                re.addFlashAttribute("step"  ,1);  
                return "redirect:/Admin/editeGroupe/" +groupeid ;
            }
            }
        }
        

            //checking the type of work

            if(!groupechanging.getTypeOfWork().equals(typeofwork)){
                if(typeofwork.equals("in Group")){
                    model.addAttribute("type", 1);
                    model.addAttribute("groupeid", groupeid);
                    model.addAttribute("listetudiants", etudiants);
                    model.addAttribute("messagfail", "merci Ajouter un candidat pour enregistrer votre modification");

                    return "updateGroupeEtudiant";
                }else{
                    model.addAttribute("type", 2);
                    model.addAttribute("groupeid", groupeid);
                    model.addAttribute("listetudiants", etudiants);
                    model.addAttribute("messagfail", "merci d'éliminer un candidat pour enregistrer votre modification");
                    return "updateGroupeEtudiant";
                }           
            }
           
            re.addFlashAttribute("messagesucces", "les nouvelles données ont été enregistrées avec succès");      
            re.addFlashAttribute("step"  ,1);
            return "redirect:/Admin/editeGroupe/"+groupeid;
        }else{

            re.addFlashAttribute("messagfail", "Le groupe qui port ID :"+groupeid+" n'a pas été trouvé ");      
            re.addFlashAttribute("step"  ,1);
            return "redirect:/Admin/editeGroupe/"+groupeid;
   
        }                                         
                                      
}

  


}
