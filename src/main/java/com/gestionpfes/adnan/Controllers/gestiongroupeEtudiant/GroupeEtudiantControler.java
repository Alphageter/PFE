package com.gestionpfes.adnan.Controllers.gestiongroupeEtudiant;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.gestionpfes.adnan.models.Encadrant;
import com.gestionpfes.adnan.models.Etudiant;
import com.gestionpfes.adnan.models.EtudiantinGroupe;
import com.gestionpfes.adnan.models.Groupe;
import com.gestionpfes.adnan.models.Request;
import com.gestionpfes.adnan.models.Subject;
import com.gestionpfes.adnan.services.EncadrantService;
import com.gestionpfes.adnan.services.EtudiantService;
import com.gestionpfes.adnan.services.EtudiantinGroupeService;
import com.gestionpfes.adnan.services.GroupeService;
import com.gestionpfes.adnan.services.RequestService;
import com.gestionpfes.adnan.services.SubjectService;

import jakarta.servlet.http.HttpSession;

@Controller

public class GroupeEtudiantControler {
    
@Autowired
private GroupeService groupeService;

@Autowired
private EtudiantinGroupeService etudiantinGroupeService;

@Autowired
private EtudiantService etudiantService;

@Autowired
private EncadrantService encadrantService;

@Autowired
private SubjectService subjectService;

@Autowired
private RequestService requestService;



//check request here and modifier the button demand and add to not display again

@GetMapping("/Etudiant/Groupe")
public String displayGroupeofEtudiant(Model model , HttpSession session){
Long userid = (Long) session.getAttribute("userID");
for(int i = 0 ; i<7 ; i++){
    System.out.println("im at the tope of /Etudiant/Groupe the user id is    !!!! "+userid);
}

EtudiantinGroupe etudiantinGroupe = etudiantinGroupeService.getetudiantinGroupesByetudiantID(userid);
if(etudiantinGroupe != null){
   

Optional<Groupe> optionalgroupe = groupeService.findById(etudiantinGroupe.getGroupeID());
if(optionalgroupe.isPresent())
{
    for(int i = 0 ; i<7 ; i++){
        System.out.println("im in groupe found so we work on it !!!!");
    }
    Groupe groupe = optionalgroupe.get();
    
    Encadrant encadrant = encadrantService.getEncadrantById(groupe.getEndarantID());
    if(encadrant!=null){
        for(int i = 0 ; i<7 ; i++){
            System.out.println("im in encadrant found so we work on Encadrant!!!!");
        }
        if(groupe.getStatus().equals("en attente")){

            //display the step 5 with info message to wait the enadrant to accepte

        model.addAttribute("messagefinfo","merci d'attendre que l'encadrant sélectionné accepte votre demande ");
        model.addAttribute("step", 5);
        return "GroupeEtudiant";
    }
        if(groupe.getStatus().equals("refuser")){

            for(int i = 0 ; i<7 ; i++){
                System.out.println("im in encadrant refuser so we work on Encadrant refuser refuser!!!!");
            }
            //the groupe was refused so they have to check new Encadrant
            
                      List<Encadrant> encadrants = encadrantService.getEncadrantByFilierAndGroupesnumberLessThanEqual(groupe.getFilier(), 10);
                    model.addAttribute("encadrants", encadrants);
                      model.addAttribute("messagfail","Refuser : sélectionnez un nouveau Encadrant");
                    model.addAttribute("step", 3);
                    return "GroupeEtudiant";
            
            }
            
        
        if(groupe.getSubjectID()==null)
        {
            for(int i = 0 ; i<7 ; i++){
                System.out.println("im in here for the first time !!!!");
            }
             //ther subject deosnt suggested or selected yet
 
             if(encadrant.getFilier().equals("SMI")){
                model.addAttribute("SMI",1);
             }else{
                model.addAttribute("SMI",2);
             }
             List<Subject> subjectsagain = subjectService.getSubjectByUsertimesSelectedLessThanEqual(encadrant,2);

             model.addAttribute("subjects",subjectsagain);
             model.addAttribute("step", 4);
             return "GroupeEtudiant";
 

        }


         Subject subject = subjectService.getSubjectById(groupe.getSubjectID());
         
         
         if(subject != null){
            for(int i = 0 ; i<7 ; i++){
                System.out.println("im in subject found so we work on subject!!!!");
            }
            

            if(subject.getStatus().equals("accepter")){
                
                for(int i = 0 ; i<7 ; i++){
                    System.out.println("im in subject accepter found so we work on subject accepter accepter accepter!!!!");
                }
                //display info of groupe


                List<Etudiant> listetudiants = etudiantService.getEtudiantByGroupeID(groupe.getId());
               
               List<Request> listrequestspartner = requestService.findByUserSenderIdAndStatus(userid, "partenaire");
               if(listrequestspartner.isEmpty()) 
               {
                for(int i = 0 ; i<7 ; i++){
                    System.out.println("im after enabling button add the groupe values !!!!"+
                    groupe.getName()+" /"+groupe.getTypeOfWork());
                }
               if(groupe.getTypeOfWork().equals("individual")){
                for(int i = 0 ; i<7 ; i++){
                    System.out.println("im enabling button add add add !!!!");
                }
                
                model.addAttribute("add", "add button");
            }
        }
        List<Request> listrequestreserver = requestService.findByUserSenderIdAndStatus(userid, "reservation");
               if(listrequestreserver.isEmpty()) 
               {
            if(groupe.isAutorisation()== false){
                for(int i = 0 ; i<7 ; i++){
                    System.out.println("im enabling button demande !!!!");
                }
                model.addAttribute("demande","vou pouver demander");
            }
        }

        //add raports if exists
          List<Subject> listraports = new ArrayList<>();
        for(Etudiant etudiantraport : listetudiants){
            for(int i = 0 ; i<7 ; i++){
                System.out.println("im in listraports chering for raports!!!! "+etudiantraport.getId());
            }
            listraports.addAll( subjectService.getSubjectsByCreateurAndStatus(etudiantraport , "raport"));

        }
        if(!listraports.isEmpty()){
            for(int i = 0 ; i<7 ; i++){
                System.out.println("im in listraports insnt emptyyy raports!!!!");
            }
            model.addAttribute("listraports", listraports);
        }
            model.addAttribute("subject",subject);
            model.addAttribute("groupedata",groupe);
            model.addAttribute("listetudiants",listetudiants);
            model.addAttribute("encadrant", encadrant);

            return "GroupeEtudiantinfo";
                
            }else if(subject.getStatus().equals("en attente")){
                //display step 5 with message iinfo that he should wait the answer)
                for(int i = 0 ; i<7 ; i++){
                    System.out.println("im in subject en attent found so we work on subject attent attent attent!!!!");
                }

                model.addAttribute("messagefinfo","merci d'attendre que Votre Encadrant  accepte votre sujet suggéré ");
                 model.addAttribute("step", 5);
                 return "GroupeEtudiant";

            }else{
                //display step 4 to select the subject again .
                for(int i = 0 ; i<7 ; i++){
                    System.out.println("im in subject en refuse found so we work on subject refuse refuse refuse!!!!");
                }

             List<Subject> subjectsagain = subjectService.getSubjectByUsertimesSelectedLessThanEqual(encadrant,2);
             if( groupe.getFilier().equals("SMI")){
                model.addAttribute("SMI", 1);
             }else{
                model.addAttribute("SMI", 2);
             }
            model.addAttribute("subjects",subjectsagain);
            model.addAttribute("step", 4);
            return "GroupeEtudiant";
            }
            
         }else{
            for(int i = 0 ; i<7 ; i++){
                System.out.println("yes im herer !!!!");
            }
            if(encadrant.getFilier().equals("SMI")){
                model.addAttribute("SMI",1);
             }else{
                model.addAttribute("SMI",2);
             }
            //ther subject deosnt suggested or selected yet

            List<Subject> subjectsagain = subjectService.getSubjectByUsertimesSelectedLessThanEqual(encadrant,2);

            model.addAttribute("subjects",subjectsagain);
            model.addAttribute("step", 4);
            return "GroupeEtudiant";

            
         }

    }else{
        
        //check the status of groupe the decide what to display

          if(groupe.getStatus().equals("en attente")){

            //display the step 5 with info message to wait the enadrant to accepte

        model.addAttribute("messagefinfo","merci d'attendre que l'encadrant sélectionné accepte votre demande ");
        model.addAttribute("step", 5);
        return "GroupeEtudiant";
    }else{
        
        //the groupe was refused so they have to check new Encadrant

        List<Encadrant> encadrants = encadrantService.getEncadrantByFilierAndGroupesnumberLessThanEqual(groupe.getFilier(), 10);
        model.addAttribute("encadrants", encadrants);
          model.addAttribute("messagfail","Refuser : sélectionnez un nouveau Encadrant");
        model.addAttribute("step", 3);
        return "GroupeEtudiant";
    }

    }

}else {

    // to check if there is a problem

    model.addAttribute("messagefinfo","il y a un problème pour trouver votre groupe");
        model.addAttribute("step", 5);
        return "GroupeEtudiant";
}

}else{

    // to create groupe from step 1 

    model.addAttribute("messagesucces","insérez les données nécessaires pour créer votre GROUPE PFE");
    model.addAttribute("step", 1);
    return "GroupeEtudiant";

}


}


}
