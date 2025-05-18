package com.gestionpfes.adnan.Controllers.gestiongroupeEtudiant;


import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

//Requestttt addd here

@Controller
public class CreateGroupeEtudiant {
    
    private static final Logger logger = LogManager.getLogger(CreateGroupeEtudiant.class);
   
    @Autowired
    private GroupeService groupeService;

    @Autowired
    private EncadrantService encadrantService;

    @Autowired
    private EtudiantService etudiantService;

    @Autowired
    private EtudiantinGroupeService etudiantinGroupeService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private RequestService requestService;

    @PostMapping("/Etudiant/createGroupe")
   public String createGroupeStep1(  Groupe groupe,
                                     @RequestParam int step,
                                     @RequestParam(required = false) String name  ,
                                     @RequestParam(required = false) String typeofwork  ,
                                     @RequestParam(required = false) String email1  ,
                                     @RequestParam(required = false) String email2,
                                     @RequestParam(required = false) Long encadrantID  ,
                                     @RequestParam(required = false)  Long subjectID  , 
                                     Model model , RedirectAttributes re
                                     ,HttpSession session) {

 try{
    switch(step){
        case 1:

         // Get the name and typeOfWork from the submitted form
       
       String nameGroupeSession = name ;
       String typeOfWorkSession = typeofwork; 
       Etudiant etudiantCheck = etudiantService.getEtudiantByEmail(email1);

        // Check if a Groupe with the same name already exists
       
        Boolean existingNameGroupe = groupeService.existsByName(name);
        if (existingNameGroupe) {
            model.addAttribute("messagfail", "Un groupe avec le même nom existe déjà");
            model.addAttribute("step", 1);
            return "GroupeEtudiant";
        }else {

            if(etudiantCheck==null){
                model.addAttribute("messagfail", "Il n'y a aucune Etudiant avec cet e-mail");
                model.addAttribute("step", 1);
                return "GroupeEtudiant";
            }
           
            EtudiantinGroupe existingGroupe =  etudiantinGroupeService.getetudiantinGroupesByetudiantID(etudiantCheck.getId());
             
            if (existingGroupe != null) {
                model.addAttribute("messagfail", "L'Étudiant sélectionné a déjà un groupe");
                model.addAttribute("step", 1);
                return "GroupeEtudiant";
                                       }
                }

                Long useridcheck = (Long) session.getAttribute("userID");
                Etudiant etudiantlogin = etudiantService.getEtudiantById(useridcheck);
                if(! etudiantlogin.getEmail().equals(email1)){
                    model.addAttribute("messagfail", "l'email donné ne correspond pas à votre email actuel ");
                model.addAttribute("step", 1);
                return "GroupeEtudiant";



                }
                
                //new modification
       session.setAttribute("EnameGroupeSession", nameGroupeSession);
       session.setAttribute("EtypeOfWorkSession", typeOfWorkSession);
       session.setAttribute("Eemail1", email1);

       // Set the type of work of the Groupe to be used in the next step of the form
   
       if(typeOfWorkSession.equals("individual")){
        // Set the step of the form to be used in the next step of the form
        model.addAttribute("messagesucces", "Étape 2 : Remplissez tous les champs");
        model.addAttribute("encadrants", encadrantService.getEncadrantByFilierAndGroupesnumberLessThanEqual
        (etudiantCheck.getFilier(),10));     
        model.addAttribute("step", 3);
    }else{
        model.addAttribute("messagesucces", "Étape 1-2 : Remplissez tous les champs");
    
        model.addAttribute("step", 2);
     }
     return "GroupeEtudiant";


     //____________ STEP 2 _____________
     case 2 :
         
              
     Etudiant etudiant2Check = etudiantService.getEtudiantByEmail(email2);
     if(etudiant2Check==null){
         model.addAttribute("messagfail", "Il n'y a pas d'étudiant avec cet e-mail");
         model.addAttribute("step", 2);
         return "GroupeEtudiant";
     }else{
         Etudiant etudiant1 = etudiantService.getEtudiantByEmail((String)session.getAttribute("Eemail1"));
         if(!etudiant1.getFilier().equals(etudiant2Check.getFilier())){
             model.addAttribute("messagfail", "l'étudiant selectionne n'a pas le meme filière de le premier etudian");
             model.addAttribute("step", 2);
              return "GroupeEtudiant";
         }

     }
      
     if (etudiant2Check.getGroupeID() != null) {
         model.addAttribute("messagfail", "L'Étudiant sélectionné a déjà un groupe");
         model.addAttribute("step", 2);
         return "GroupeEtudiant";
      }


      session.setAttribute("Eemail2", email2);
      model.addAttribute("encadrants", encadrantService.getEncadrantByFilierAndGroupesnumberLessThanEqual(etudiant2Check.getFilier(),10));     
      model.addAttribute("messagesucces", "Étape 2 : Remplissez tous les champs");
      model.addAttribute("step", 3);



      Etudiant etudiantsender = etudiantService.getEtudiantByEmail((String)session.getAttribute("Eemail1"));
         
      Request requestpartener = new Request();
      requestpartener.setSeen(false);
      requestpartener.setStatus("partenaire");
      requestpartener.setSubject("M."+etudiantsender.getLastname() +" vous ajoute à son groupe pour PFE" );
      requestpartener.setUserSenderId(etudiantsender.getId());
      requestpartener.setUserGeterId(etudiant2Check.getId());

 
      requestService.createRequest(requestpartener);

     
      return "GroupeEtudiant";


      //____________ STEP 3_____________
      case 3:

        
      Encadrant encadrant = encadrantService.getEncadrantById(encadrantID) ;

           if(encadrant!=null){
                
                Long userid = (Long) session.getAttribute("userID");
                Etudiant etudiant = etudiantService.getEtudiantById(userid);
                if(etudiant.getGroupeID()==null){
                    // modifier it
                    
                    // Create new Object of groupe and inser the value from session and delete them at the end
                    Etudiant etudiant1set = etudiantService.getEtudiantByEmail((String)session.getAttribute("Eemail1"));
                    Etudiant etudiant2set = etudiantService.getEtudiantByEmail((String)session.getAttribute("Eemail2"));
                    String typeofworkset = (String) session.getAttribute("EtypeOfWorkSession");
                    String nameset = (String) session.getAttribute("EnameGroupeSession");
                    // Encadrant encadrantset = encadrantService.getEncadrantById((Long)session.getAttribute("EencadrantID"));
                     
                 
                       

                    Groupe newgroupe = new Groupe() ;

                    newgroupe.setAutorisation(false);
                    newgroupe.setStatus("en attente");
                    newgroupe.setName(nameset);
                    newgroupe.setTypeOfWork(typeofworkset);
                    newgroupe.setPresentationDate(null);
                    newgroupe.setFilier(etudiant1set.getFilier());
                    newgroupe.setEndarantID(encadrant.getId());
                    newgroupe.setSubjectID(null); 

                    

                    newgroupe = groupeService.createGroupe(newgroupe);

                   
      

                    if(etudiant1set!=null){
                        etudiantService.updateEtudiantGroupeID(etudiant1set.getId(), newgroupe.getId());

                        EtudiantinGroupe etudiantinGroupe = new EtudiantinGroupe();
                        etudiantinGroupe.setGroupeID(newgroupe.getId());
                        etudiantinGroupe.setEtudiantID(etudiant1set.getId());
                        etudiantinGroupeService.createetudiantinGroupe(etudiantinGroupe);

                    }
                    if(etudiant2set!=null){
                        etudiantService.updateEtudiantGroupeID(etudiant2set.getId(), newgroupe.getId());

                        EtudiantinGroupe etudiantinGroupe = new EtudiantinGroupe();
                        etudiantinGroupe.setGroupeID(newgroupe.getId());
                        etudiantinGroupe.setEtudiantID(etudiant2set.getId());
                        etudiantinGroupeService.createetudiantinGroupe(etudiantinGroupe);
                    }
                    
                    Request requestdemandEncadrant = new Request();
                    requestdemandEncadrant.setSeen(false);
                    requestdemandEncadrant.setStatus("demander");
                    requestdemandEncadrant.setSubject("M."+etudiant1set.getLastname() +"vous demande d'être son encadrant pour PFE" );
                    requestdemandEncadrant.setUserSenderId(etudiant1set.getId());
                    requestdemandEncadrant.setUserGeterId(encadrant.getId());
              
               
                    requestService.createRequest(requestdemandEncadrant);
               
                    model.addAttribute("messagefinfo","merci d'attendre que l'encadrant sélectionné accepte votre demande pour sélectionner votre sujet");
                    model.addAttribute("step", 5);
                    return "GroupeEtudiant";
                }else{
                    
                    groupeService.updateGroupeStatus(etudiant.getGroupeID(),"en attente");
                    groupeService.updateGroupeEndarantID(etudiant.getGroupeID(), encadrant.getId());



                    Request requestdemandEncadrant = new Request();
                    requestdemandEncadrant.setSeen(false);
                    requestdemandEncadrant.setStatus("demander");
                    requestdemandEncadrant.setSubject("M."+etudiant.getLastname() +"vous demande d'être son encadrant pour PFE" );
                    requestdemandEncadrant.setUserSenderId(etudiant.getId());
                    requestdemandEncadrant.setUserGeterId(encadrant.getId());
              
               
                    requestService.createRequest(requestdemandEncadrant);

                    model.addAttribute("messagefinfo","merci d'attendre que l'encadrant sélectionné accepte votre demande pour sélectionner votre sujet");
                    model.addAttribute("step", 5);
                    return "GroupeEtudiant";

                    }

           }else{
               model.addAttribute("messagfail", "L'Encadrant n'existe pas !!");
               model.addAttribute("step", 3);
               return "GroupeEtudiant";
           }
           case 4:
     
          
           Subject subjectset =subjectService.getSubjectById(subjectID);
           if(subjectset!=null){
            Long userid = (Long) session.getAttribute("userID");
             Etudiant etudiant = etudiantService.getEtudiantById(userid);
            Optional<Groupe> optionalgroupe = groupeService.findById(etudiant.getGroupeID());
            if(optionalgroupe.isPresent()){
                Groupe groupesubject = optionalgroupe.get();
                groupeService.updateGroupeSubjectID(groupesubject.getId(), subjectset.getId());
                subjectService.updateTimesSelected(subjectset.getId(), subjectset.getTimesSelected()+1);

                re.addFlashAttribute("messagesucces", "le sujet a été choisi avec succès ");
                  
                   return "redirect:/Etudiant/Groupe";
            }else{

                model.addAttribute("messagefinfo","il y a un problème pour trouver votre groupe");
                model.addAttribute("step", 5);
                return "GroupeEtudiant";
            }


        }else{
            model.addAttribute("messagfail", "Le Sujet n'existe pas !!");
            model.addAttribute("step", 4);
            return "GroupeEtudiant";


        }

        default:
        model.addAttribute("messagfail", "il y a un problème avec la compilation des données");
        model.addAttribute("groupe", groupe);
        model.addAttribute("step", step);
        return "GroupeEtudiant";

    }
    
    }catch(Exception ex){

        logger.error("Error occurred during subject update", ex);
          ex.printStackTrace();
          return "GroupeEtudiant";
     }

 



                                     
                                    
                                    }
    
}
