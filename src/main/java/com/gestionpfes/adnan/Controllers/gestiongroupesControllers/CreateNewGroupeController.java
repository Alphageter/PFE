package com.gestionpfes.adnan.Controllers.gestiongroupesControllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;



@Controller

public class CreateNewGroupeController {
   
    private static final Logger logger = LogManager.getLogger(CreateNewGroupeController.class);
   
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
    
    //add ajouter request here
   
   @PostMapping("/Admin/createGroupe")
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
           return "newGroupe";
       }else {
           
         
          
           if(etudiantCheck==null){
               model.addAttribute("messagfail", "Il n'y a aucune Etudiant avec cet e-mail");
               model.addAttribute("step", 1);
               return "newGroupe";
           }
           EtudiantinGroupe existingGroupe =  etudiantinGroupeService.getetudiantinGroupesByetudiantID(etudiantCheck.getId());
            
           if (existingGroupe != null) {
               model.addAttribute("messagfail", "L'Étudiant sélectionné a déjà un groupe");
               model.addAttribute("step", 1);
               return "newGroupe";
                                      }
           }
       
     
    //new modification
       session.setAttribute("nameGroupeSession", nameGroupeSession);
       session.setAttribute("typeOfWorkSession", typeOfWorkSession);
       session.setAttribute("email1", email1);

   
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
        return "newGroupe";
    
         
       
   
           //____________ STEP 2 _____________
         case 2 :
         
              
               Etudiant etudiant2Check = etudiantService.getEtudiantByEmail(email2);
               if(etudiant2Check==null){
                   model.addAttribute("messagfail", "Il n'y a pas d'étudiant avec cet e-mail");
                   model.addAttribute("step", 2);
                   return "newGroupe";
               }else{
                   Etudiant etudiant1 = etudiantService.getEtudiantByEmail((String)session.getAttribute("email1"));
                   if(!etudiant1.getFilier().equals(etudiant2Check.getFilier())){
                       model.addAttribute("messagfail", "l'étudiant selectionne n'a pas le meme filière de le premier etudian");
                       model.addAttribute("step", 2);
                        return "newGroupe";
                   }
   
               }
                
               if (etudiant2Check.getGroupeID() != null) {
                   model.addAttribute("messagfail", "L'Étudiant sélectionné a déjà un groupe");
                   model.addAttribute("step", 2);
                   return "newGroupe";
                }


                session.setAttribute("email2", email2);
                model.addAttribute("encadrants", encadrantService.getEncadrantByFilierAndGroupesnumberLessThanEqual(etudiant2Check.getFilier(),10));     
                model.addAttribute("messagesucces", "Étape 2 : Remplissez tous les champs");
                model.addAttribute("step", 3);
               
                return "newGroupe";
   
           
   
   
           //____________ STEP 3_____________
          case 3:

        
          Encadrant encadrant = encadrantService.getEncadrantById(encadrantID) ;
   
               if(encadrant!=null){
                    //encadrant.setGroupesnumber(encadrant.getGroupesnumber()+1);
                    session.setAttribute("encadrantID", encadrantID);
                   
                   List<Subject> subjects = subjectService.getSubjectByUsertimesSelectedLessThanEqual(encadrant,2);
                
                model.addAttribute("subjects", subjects);
                model.addAttribute("step", 4);
                model.addAttribute("messagesucces", "Étape 3 : Remplissez tous les champs");
                     
        return "newGroupe";
   
               }else{
                   model.addAttribute("messagfail", "L'Encadrant n'existe pas !!");
                   model.addAttribute("step", 3);
                   return "newGroupe";
               }
   
           
           case 4:
     
          
               Subject subjectset =subjectService.getSubjectById(subjectID);
               if(subjectset!=null){
                    
                    
                    // Create new Object of groupe and inser the value from session and delete them at the end
                    Etudiant etudiant1set = etudiantService.getEtudiantByEmail((String)session.getAttribute("email1"));
                    Etudiant etudiant2set = etudiantService.getEtudiantByEmail((String)session.getAttribute("email2"));
                    String typeofworkset = (String) session.getAttribute("typeOfWorkSession");
                    String nameset = (String) session.getAttribute("nameGroupeSession");
                    Encadrant encadrantset = encadrantService.getEncadrantById((Long)session.getAttribute("encadrantID"));
                     
                 
                       

                    Groupe newgroupe = new Groupe() ;

                    newgroupe.setAutorisation(false);
                    newgroupe.setStatus("accepter");
                    newgroupe.setName(nameset);
                    newgroupe.setTypeOfWork(typeofworkset);
                    newgroupe.setPresentationDate(null);
                    newgroupe.setFilier(etudiant1set.getFilier());
                    newgroupe.setSubjectID(subjectset.getId());
                    newgroupe.setEndarantID(encadrantset.getId()); 

                    encadrantService.updateEncadrantGroupesNumber(encadrantset.getId(), encadrantset.getGroupesnumber()+1);

                    subjectService.updateTimesSelected(subjectset.getId(), subjectset.getTimesSelected()+1);

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
                    Long adminid = (Long) session.getAttribute("userID");
                    
                     //send request to encadrant

                    Request requestajouter  = new Request();
                    requestajouter.setSeen(false);
                    requestajouter.setStatus("ajouter");
                    requestajouter.setSubject("le groupe : "+nameset+"  a été ajouté à votre liste de groupes"); 
                    requestajouter.setUserSenderId(adminid); 
                    requestajouter.setUserGeterId(encadrantset.getId());
                    
                    requestService.createRequest(requestajouter);
                    
                    if(etudiant1set!=null){
                    //send request to etudiant1
                
                    Request requestajouteretud1  = new Request();
                    requestajouteretud1.setSeen(false);
                    requestajouteretud1.setStatus("ajouter");
                    requestajouteretud1.setSubject("vous avez été ajouté au groupe de PFE : "+nameset); 
                    requestajouteretud1.setUserSenderId(adminid); 
                    requestajouteretud1.setUserGeterId(etudiant1set.getId());
                   
                    requestService.createRequest(requestajouteretud1);

                    }
                    if(etudiant2set!=null){
                        //send request to etudiant2
    
                        Request requestajouteretud2    = new Request();
                        requestajouteretud2  .setSeen(false);
                        requestajouteretud2  .setStatus("ajouter");
                        requestajouteretud2  .setSubject("vous avez été ajouté au groupe de PFE : "+nameset); 
                        requestajouteretud2  .setUserSenderId(adminid); 
                        requestajouteretud2  .setUserGeterId(etudiant2set.getId());
                        
                        requestService.createRequest(requestajouteretud2);
    
    
                        }
                

                    session.removeAttribute("email1");
                    session.removeAttribute("email2");
                    session.removeAttribute("typeOfWorkSession");
                    session.removeAttribute("nameGroupeSession");
                    session.removeAttribute("encadrantID");
                    
                   
                   re.addFlashAttribute("messagesucces", "les données du groupe ont été insérées avec succès ");
                  
                   return "redirect:/Admin/groupes/list";
   
   
               }else{
                   model.addAttribute("messagfail", "Le Sujet n'existe pas !!");
                   model.addAttribute("step", 4);
                   return "newGroupe";
   
   
               }
   
                
           default:
           model.addAttribute("messagfail", "il y a un problème avec la compilation des données");
           model.addAttribute("groupe", groupe);
           model.addAttribute("step", step);
           return "newGroupe";
        
   }
}catch(Exception ex){
    
    logger.error("Error occurred during subject update", ex);
      ex.printStackTrace();

return "newGroupe";
}
}
   




//DOWNLOAD the pdf file 
@GetMapping("/Admin/download/subject/{subjectId}")
public void viewPdf(@PathVariable Long subjectId, HttpServletResponse response) throws IOException {
    
       Subject subject = subjectService.getSubjectById(subjectId);
       File pdfFile = new File(subject.getPdfFile()); 
         // Assuming that the pdfFile property of Subject entity contains the file path of the PDF
         response.setContentType("application/pdf");
         response.setHeader("Content-disposition", "inline; filename=" + pdfFile.getName());
           FileInputStream inputStream = new FileInputStream(pdfFile);
          IOUtils.copy(inputStream, response.getOutputStream());
        response.flushBuffer();

            }
   
}
