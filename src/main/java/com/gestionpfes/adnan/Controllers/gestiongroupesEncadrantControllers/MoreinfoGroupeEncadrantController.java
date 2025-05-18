package com.gestionpfes.adnan.Controllers.gestiongroupesEncadrantControllers;

import java.util.ArrayList;
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
public class MoreinfoGroupeEncadrantController {

    @Autowired
    private GroupeService groupeService;

    

    @Autowired
    private EtudiantService etudiantService;

    @Autowired
    private  EncadrantService encadrantService;

    @Autowired
    private SubjectService subjectService ;



    

    @GetMapping("/Encadrant/moreinfoGroupe/{groupeid}")
    public String moreinfogroupe(@PathVariable("groupeid") Long groupeid , Model model
                              , RedirectAttributes re
                                   ){

                Optional<Groupe> optionalgroupe = groupeService.findById(groupeid);
                if(optionalgroupe.isPresent()){
                Groupe groupedata = optionalgroupe.get();

                List<Etudiant> listetudiants = etudiantService.getEtudiantByGroupeID(groupedata.getId());

                Encadrant encadrant = encadrantService.getEncadrantById(groupedata.getEndarantID());

                
                if(groupedata.getSubjectID() !=null){
                    
                   Subject subject = subjectService.getSubjectById(groupedata.getSubjectID());
                      model.addAttribute("subject", subject);
                    
                }

                //add raports if exists
             List<Subject> listraports = new ArrayList<>();
              for(Etudiant etudiantraport : listetudiants){
                listraports.addAll( subjectService.getSubjectsByCreateurAndStatus(etudiantraport , "raport"));
  
          }
          if(!listraports.isEmpty()){
              model.addAttribute("listraports", listraports);
          }


                model.addAttribute("groupedata", groupedata);
                model.addAttribute("listetudiants", listetudiants);
                model.addAttribute("encadrant", encadrant);
                
                
                return "moreinfoGroupeEncadrant";

              }else {
 
                re.addFlashAttribute("messagfail", "Le groupe qui port ID :"+groupeid+" n'a pas été trouvé ");
   
                return "redirect:/Encadrant/Groupes";
                      
                      }
    }
    
}
