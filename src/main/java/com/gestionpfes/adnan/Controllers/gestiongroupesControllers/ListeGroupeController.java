package com.gestionpfes.adnan.Controllers.gestiongroupesControllers;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gestionpfes.adnan.models.Encadrant;
import com.gestionpfes.adnan.models.Etudiant;
import com.gestionpfes.adnan.models.EtudiantinGroupe;
import com.gestionpfes.adnan.models.Groupe;
import com.gestionpfes.adnan.models.Subject;
import com.gestionpfes.adnan.services.EncadrantService;
import com.gestionpfes.adnan.services.EtudiantService;
import com.gestionpfes.adnan.services.EtudiantinGroupeService;
import com.gestionpfes.adnan.services.GroupeService;
import com.gestionpfes.adnan.services.SubjectService;

import jakarta.servlet.http.HttpSession;




@Controller
public class ListeGroupeController {
   
    private static final Logger logger = LogManager.getLogger(ListeGroupeController.class);
    @Autowired
    private GroupeService groupeService;

    @Autowired
    private EtudiantinGroupeService etudiantinGroupeService;
    
    @Autowired
    private EtudiantService etudiantService;

    @Autowired
    private EncadrantService encadrantService;

    @Autowired
    private SubjectService  subjectService ;
    

    

    @GetMapping("/Admin/groupes/list")
    public String listGroupe(Model model) {
        try{
       
        List<Groupe> groupes = groupeService.findAll(Sort.by("filier"));
        model.addAttribute("listgroupes", groupes);
     
    
        return "listGroupe";}catch(Exception e){

           
            logger.error("Error occurred during subject update", e);
            e.printStackTrace();
            model.addAttribute("messagfail", e);
            return "profileAdmin";


        }
    }

    @GetMapping("/Admin/newGroupe")
    public String newGroupepage(Model model) {
        model.addAttribute("groupe", new Groupe());
        model.addAttribute("step", 1);
        return "newGroupe";
    }
   
   
    @GetMapping("/Admin/deleteGroupe/{groupeid}")
   public String deletegroupe(@PathVariable("groupeid") Long groupeid,RedirectAttributes ra){
   

        Optional<Groupe> groupe = groupeService.findById(groupeid);
        if (groupe.isPresent()) {

            Long subjectId = groupe.map(Groupe::getSubjectID).orElse(null);
            Long EncadrantId = groupe.map(Groupe::getEndarantID).orElse(null);
            Encadrant encadrant = encadrantService.getEncadrantById(EncadrantId);
            encadrantService.updateEncadrantGroupesNumber(EncadrantId, encadrant.getGroupesnumber()-1);

            Subject subject = subjectService.getSubjectById(subjectId);
            if(subject !=null){

                subjectService.updateTimesSelected(subjectId, subject.getTimesSelected()-1);
               
            }
            List<EtudiantinGroupe> etudiantsdelete  = etudiantinGroupeService.getetudiantinGroupesByGroupeID(groupeid);
           
  
if(!etudiantsdelete.isEmpty()){
   for (EtudiantinGroupe etudiantinGroupe : etudiantsdelete) {
    
     etudiantService.updateEtudiantGroupeID(etudiantinGroupe.getEtudiantID(), null);
      
       etudiantinGroupeService.deleteetudiantinGroupeById(etudiantinGroupe.getId());
      
   }
}
     
      groupeService.deleteGroupe(groupeid);
  
        
   ra.addFlashAttribute("messagesucces", "Le groupe qui port ID :"+groupeid+" a ete Supprimer avec succes ");
   
   return "redirect:/Admin/groupes/list";
}
else{
    ra.addFlashAttribute("messagfail", "Le groupe qui port ID :"+groupeid+" n'a pas été trouvé ");
   
   return "redirect:/Admin/groupes/list";


   }
   }

   @GetMapping("/Admin/editeGroupe/{groupeid}")
   public String editeGroupepage(@PathVariable("groupeid") Long groupeid,
                               Model model,
                                RedirectAttributes ra,
                                HttpSession session) {
      Optional<Groupe> optionalgroupe = groupeService.findById(groupeid);
      if(optionalgroupe.isPresent()){
        Groupe groupe = optionalgroupe.get();
 
        List<Etudiant> listEtudiant = etudiantService.getEtudiantByGroupeID(groupeid);
        Etudiant etudiant1 = listEtudiant.get(0);
        Etudiant etudiant2=null;
        if(listEtudiant.size()>1){
         etudiant2 = listEtudiant.get(1);
        }

        session.setAttribute("groupeid", groupeid);
        List<Encadrant> listEcadrants = encadrantService.getEncadrantByFilierAndGroupesnumberLessThanEqual(etudiant1.getFilier(), 10);
        Encadrant encadrant = encadrantService.getEncadrantById(groupe.getEndarantID());
        List<Subject> listsubject = subjectService.getSubjectsByCreateur(encadrant);
        // String namegroupe = groupe.getName();
        // String 
        // Subject subject = subjectService.getSubjectById(groupe.getSubjectID());
       model.addAttribute("groupeid",groupeid);
       model.addAttribute("groupe", groupe);
       model.addAttribute("etudiant1", etudiant1);
       model.addAttribute("etudiant2", etudiant2);
       model.addAttribute("listEcadrants", listEcadrants);
       model.addAttribute("listsubject", listsubject);
       model.addAttribute("step", 1);
       return "updateGroupe";
      }else{

        ra.addFlashAttribute("messagfail", "Le groupe qui port ID :"+groupeid+" n'a pas été trouvé ");
   
         return "redirect:/Admin/groupes/list";
      }
   }


}