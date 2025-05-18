package com.gestionpfes.adnan.Controllers.gestiongroupesControllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gestionpfes.adnan.models.Encadrant;
import com.gestionpfes.adnan.models.Groupe;
import com.gestionpfes.adnan.models.Request;
import com.gestionpfes.adnan.models.Subject;
import com.gestionpfes.adnan.services.EncadrantService;

import com.gestionpfes.adnan.services.GroupeService;
import com.gestionpfes.adnan.services.RequestService;
import com.gestionpfes.adnan.services.SubjectService;
import jakarta.servlet.http.HttpSession;

@Controller
public class UpdateGroupeEncadrant {
    

    @Autowired
    private GroupeService groupeService;

    @Autowired
    private EncadrantService encadrantService;


    @Autowired
    private RequestService requestService;

    @Autowired
    private SubjectService subjectService;

    //add request here ajoute and sujet changed

@PostMapping("/Admin/editeGroupeEncadrant")
public String updategroupeEncadrant(@RequestParam (required = false)int step,
                                    @RequestParam(required = false) Long groupeid  ,
                                    @RequestParam(required = false) Long encadrantID  ,
                                    Model model , RedirectAttributes re
                                   ,HttpSession session)
{
    Long groupeidsession = (Long) session.getAttribute("groupeid");
    
    Optional<Groupe> optionalgroupe = groupeService.findById(groupeidsession);
    Groupe groupechanging = optionalgroupe.get();
    List<Encadrant> listencadrant = encadrantService.getEncadrantsByFilier(groupechanging.getFilier());
if(encadrantID!=null){
    
Encadrant encadrant = encadrantService.getEncadrantById(encadrantID);
List<Subject> listsubject = subjectService.getSubjectsByCreateur(encadrant);
           

          model.addAttribute("messagfail", "merci de sélectionnez un nouveau sujet pour enregistrer votre modification");
          model.addAttribute("step"  ,3);
          model.addAttribute("listsubject"  ,listsubject);//listEcadrants   listsubject      
          model.addAttribute("groupid",groupeid);
          return "updateGroupe";

    
}else{
   
    model.addAttribute("messagfail", "l'Encadrant se ne trouve pas");
    model.addAttribute("step"  ,2); 
    model.addAttribute("listEcadrants"  ,listencadrant); 
    model.addAttribute("groupid",groupeid);
    return "updateGroupe";

}

}

@PostMapping("/Admin/editeGroupeSujet")
public String updategroupeSujet(@RequestParam (required = false)int step,
                                 @RequestParam(required = false) Long groupeid  ,
                                 @RequestParam(required = false) Long subjectID  ,
                                  Model model , RedirectAttributes re
                                 ,HttpSession session){

Long groupeidsession = (Long) session.getAttribute("groupeid");
    
if(subjectID!= null){
    
Subject subjectnew = subjectService.getSubjectById(subjectID);
Optional<Groupe> optionalgroupe = groupeService.findById(groupeidsession);
if(optionalgroupe.isPresent()){
    
    Groupe groupechanging = optionalgroupe.get();
    Subject subjectold = subjectService.getSubjectById(groupechanging.getSubjectID());
    subjectService.updateTimesSelected(subjectold.getId(), subjectold.getTimesSelected()-1);
    groupeService.updateGroupeSubjectID(groupechanging.getId(), subjectnew.getId());
if(!subjectnew.getCreateur().getId().equals(groupechanging.getEndarantID())){
    
    Encadrant encarantold = encadrantService.getEncadrantById(groupechanging.getEndarantID());
    
    encadrantService.updateEncadrantGroupesNumber(encarantold.getId(), encarantold.getGroupesnumber()-1);
   
    
    groupeService.updateGroupeEndarantID(groupechanging.getId(), subjectnew.getCreateur().getId());

    Long adminid = (Long) session.getAttribute("userID");

    Encadrant encarantnew = encadrantService.getEncadrantById(subjectnew.getCreateur().getId());
                    
                     //send request to encadrant

                    Request requestajouter  = new Request();
                    requestajouter.setSeen(false);
                    requestajouter.setStatus("ajouter");
                    requestajouter.setSubject("le groupe : "+groupechanging.getName()+"  a été ajouté à votre liste de groupes"); 
                    requestajouter.setUserSenderId(adminid);
                    requestajouter.setUserGeterId(encarantnew.getId());
                    
                    requestService.createRequest(requestajouter);
    

    re.addFlashAttribute("messagesucces", "le groupe a un nouveau Encadrant/ et sujet");
                
          return "redirect:/Admin/editeGroupe/"+groupeidsession;
          
   
}
re.addFlashAttribute("messagesucces", "le groupe a un nouveau Encadrant/ et sujet");
         
                
          return "redirect:/Admin/editeGroupe/"+groupeidsession;

}else{

    model.addAttribute("messagfail", "le groupe se ne trouve pas");
    model.addAttribute("step"  ,3); 
    model.addAttribute("groupid",groupeid);
     
    return "updateGroupe";
                   
}

}
model.addAttribute("messagfail", "il y a quelques problèmes lors de la modification");
model.addAttribute("step"  ,3);
model.addAttribute("groupid",groupeid); 
return "updateGroupe";
}


@GetMapping("/Admin/nextstep/{step}")
public String nextStep(  @PathVariable("step") int step,Model model ,
 RedirectAttributes re,HttpSession session){
Long groupeid = (Long) session.getAttribute("groupeid");

    if(step<3){
int nextStep=step+1;

 Optional<Groupe> optionalgroupe = groupeService.findById(groupeid);
 Groupe groupecheck = optionalgroupe.get();

    if(nextStep == 2){
        List<Encadrant> listEcadrants = encadrantService.getEncadrantByFilierAndGroupesnumberLessThanEqual(groupecheck.getFilier(), 10);
        Encadrant encadrant = encadrantService.getEncadrantById(groupecheck.getEndarantID());
        List<Subject> listsubject = subjectService.getSubjectsByCreateur(encadrant);
        model.addAttribute("listEcadrants", listEcadrants);
        model.addAttribute("listsubject", listsubject);
        model.addAttribute("step", 2);
        model.addAttribute("groupid",groupeid);
        return "updateGroupe";

    }else if(nextStep == 3){

        Encadrant encadrant = encadrantService.getEncadrantById(groupecheck.getEndarantID());
        List<Subject> listsubject = subjectService.getSubjectsByCreateur(encadrant);
        model.addAttribute("listsubject", listsubject);
        model.addAttribute("step", 3);
        model.addAttribute("groupid",groupeid);
        return "updateGroupe";

    }



    }
    
    
    model.addAttribute("messagefinfo","il n'y a plus des étape");
    model.addAttribute("step", 4);
    model.addAttribute("groupid",groupeid);
    
    return "updateGroupe";
    
}






}





