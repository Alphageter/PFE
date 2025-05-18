package com.gestionpfes.adnan.Controllers.gestiongroupesControllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gestionpfes.adnan.models.Encadrant;
import com.gestionpfes.adnan.models.Etudiant;
import com.gestionpfes.adnan.models.Groupe;
import com.gestionpfes.adnan.models.Subject;
import com.gestionpfes.adnan.services.EncadrantService;
import com.gestionpfes.adnan.services.EtudiantService;
import com.gestionpfes.adnan.services.GroupeService;
import com.gestionpfes.adnan.services.SubjectService;

@Controller
public class MoreinfoGroupeController {

    @Autowired
    private GroupeService groupeService;

    

    @Autowired
    private EtudiantService etudiantService;

    @Autowired
    private  EncadrantService encadrantService;

    @Autowired
    private SubjectService subjectService ;



    // /Admin/moreinfoGroupe/' + ${group.id}

    @GetMapping("/Admin/moreinfoGroupe/{groupeid}")
    public String moreinfogroupe(@PathVariable("groupeid") Long groupeid , Model model
                              , RedirectAttributes re
                                   ){
                                    for(int i =0 ;i<7;i++){
                                      System.out.println("im in the controller!!!!!!!! ");
                                  }

                Optional<Groupe> optionalgroupe = groupeService.findById(groupeid);
                if(optionalgroupe.isPresent()){
                 
                Groupe groupedata = optionalgroupe.get();
 for(int i =0 ;i<7;i++){
                    System.out.println("we found groupe!!!!!!!! "+groupedata.getId());
                }
                List<Etudiant> listetudiants = etudiantService.getEtudiantByGroupeID(groupedata.getId());
                
                Encadrant encadrant = encadrantService.getEncadrantById(groupedata.getEndarantID());
                for(int i =0 ;i<7;i++){
                  System.out.println("i need your email mR 11 !!!!!!!! "+encadrant.getEmail());
              }
             
                if(groupedata.getStatus().equals("accepter"))
                {
                  for(int i =0 ;i<7;i++){
                    System.out.println("i should not be here 11 !!!!!!!! ");
                }
                 
                  model.addAttribute("encadrant", encadrant);
              }
              if(groupedata.getSubjectID()!=null){

                for(int i =0 ;i<7;i++){
                  System.out.println("i should not be here 22 !!!!!!!! ");
              }
                Subject subject = subjectService.getSubjectById(groupedata.getSubjectID());
               
                if(subject.getStatus().equals("accepter")){
                 
                  for(int i =0 ;i<7;i++){
                    System.out.println("am i herrreee 3333 !!!!!!!! "+subject.getId()+" : /"+subject.getName());
                }
                  model.addAttribute("subject", subject);

                }
              }
                

              for(int i =0 ;i<7;i++){
                System.out.println("if i m here the should check html !!!!!!!! ");
            }
                model.addAttribute("groupedata", groupedata);
                model.addAttribute("listetudiants", listetudiants);
                
                
                
                return "moreinfoGroupe";

              }else {
 
                re.addFlashAttribute("messagfail", "Le groupe qui port ID :"+groupeid+" n'a pas été trouvé ");
   
                return "redirect:/Admin/groupes/list";
                      
                      }
    }
    
}
