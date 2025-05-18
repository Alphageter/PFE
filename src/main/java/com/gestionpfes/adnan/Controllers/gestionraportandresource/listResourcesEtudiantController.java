package com.gestionpfes.adnan.Controllers.gestionraportandresource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.gestionpfes.adnan.models.Encadrant;
import com.gestionpfes.adnan.models.Etudiant;
import com.gestionpfes.adnan.models.Groupe;
import com.gestionpfes.adnan.models.Subject;
import com.gestionpfes.adnan.services.EncadrantService;
import com.gestionpfes.adnan.services.EtudiantService;
import com.gestionpfes.adnan.services.GroupeService;
import com.gestionpfes.adnan.services.SubjectService;

import jakarta.servlet.http.HttpSession;

@Controller
public class listResourcesEtudiantController {
    
    @Autowired
    private SubjectService subjectService;

    @Autowired
    private EtudiantService etudiantService;

    @Autowired
    private EncadrantService encadrantService ;

    @Autowired
    private GroupeService groupeService;


    @GetMapping("/Etudiant/resources")
    public String showSubjects(Model model , HttpSession session ) {
        Etudiant etudiant = etudiantService.getEtudiantById((Long)session.getAttribute("userID"));
        Groupe groupe = groupeService.findById(etudiant.getGroupeID()).orElse(null);
        //dont forget it
        if(groupe != null){
        Encadrant encadrant = encadrantService.getEncadrantById(groupe.getEndarantID());
        List<Subject> listdocuments = subjectService.getSubjectsByCreateurAndStatus(encadrant, "document");
        if(!listdocuments.isEmpty()){
            model.addAttribute("listdocuments", listdocuments);
         }
        
         List<Subject> listlines = subjectService.getSubjectsByCreateurAndStatus(encadrant, "line");
         if(!listlines.isEmpty()){
          for(int i = 0;i<6;i++){
            System.out.println("im in listlines not empty");
          }
            model.addAttribute("listlines", listlines);
             }

        return "listResourceEtudiant";

    }else{
        model.addAttribute("messagefinfo","il y a un problème pour trouver votre groupe");
        model.addAttribute("step", 5);
        return "GroupeEtudiant";

    }

    }

}
