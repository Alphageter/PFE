package com.gestionpfes.adnan.Controllers.gestionRequets;


import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gestionpfes.adnan.models.Encadrant;
import com.gestionpfes.adnan.models.Etudiant;
import com.gestionpfes.adnan.models.Groupe;
import com.gestionpfes.adnan.models.Request;
import com.gestionpfes.adnan.models.Subject;
import com.gestionpfes.adnan.models.User;
import com.gestionpfes.adnan.services.EncadrantService;
import com.gestionpfes.adnan.services.EtudiantService;
import com.gestionpfes.adnan.services.GroupeService;
import com.gestionpfes.adnan.services.RequestService;
import com.gestionpfes.adnan.services.SubjectService;
import com.gestionpfes.adnan.services.UserService;

import ch.qos.logback.core.model.Model;
import jakarta.servlet.http.HttpSession;


@Controller
public class RequestsEncadrantController {
    

    @Autowired
    private RequestService requestService;
    
    @Autowired
    private UserService userService;

    @Autowired
    private EtudiantService etudiantService;

     @Autowired
     private GroupeService groupeService;

     @Autowired
     private EncadrantService encadrantService ;

     @Autowired
     private SubjectService subjectService;







     //mark seen
    
    @GetMapping("/Encadrant/markevu/{id}")
    public String markeseenRequest(@PathVariable("id") Long id, Model model, RedirectAttributes ra){
        for(int i = 0 ; i <7 ; i++){

            System.out.println("i m at the top of profil encadrant controler");
        }

    Request request = requestService.findById(id).orElse(null);
    if(request !=null){

        requestService.updateSeen(id, true);
       
        return "redirect:/Encadrant/profile";

    }
    else{
         ra.addFlashAttribute("messagfail","il ya des problémes to trouver cette message dans votre boit");
         return "redirect:/Encadrant/profile";
        }
           
    }

    // delete message

@GetMapping("/Encadrant/deletemsg/{id}")
public String deleteRequest(@PathVariable("id") Long id, Model model, RedirectAttributes ra){
    Request request = requestService.findById(id).orElse(null);
    if(request !=null){
        
            requestService.deleteRequestById(request.getId());
       
        return "redirect:/Encadrant/profile";

    }
    else{
         ra.addFlashAttribute("messagfail","il ya des probleme to trouver cette message dans votre boit");
         return "redirect:/Encadrant/profile";
        }

}

    //accepte Request o groupre or suggestion

    @GetMapping("/Encadrant/accepter/{id}")
    public String accepteterRequest(@PathVariable("id") Long id, Model model, RedirectAttributes ra
    , HttpSession session){
   
        Long userid = (Long) session.getAttribute("userID");
        Request request = requestService.findById(id).orElse(null);
    if(request !=null){
         User usersender = userService.getUserById(request.getUserSenderId());
            
            Etudiant etudiantsender = etudiantService.getEtudiantById(usersender.getId());
            if(etudiantsender != null)
            {

        if(request.getStatus().equals("demander"))
         {
            

            Groupe groupeaccepter = groupeService.findById(etudiantsender.getGroupeID()).orElse(null);
            if(groupeaccepter !=null){
                groupeService.updateGroupeStatus(groupeaccepter.getId(), "accepter");
                Encadrant encadrant = encadrantService.getEncadrantById(userid);
                encadrantService.updateEncadrantGroupesNumber(userid, encadrant.getGroupesnumber()+1);

                Request requestanswer = new Request();

                requestanswer.setSeen(false);
                requestanswer.setStatus("accepter");
                requestanswer.setSubject("Mr."+encadrant.getLastname()+" a accepet votre demande vous pouvez maintenant sélectionner le sujet pour votre PFE" );
                requestanswer.setUserSenderId(encadrant.getId());
                requestanswer.setUserGeterId(etudiantsender.getId());

                requestService.createRequest(requestanswer);

                requestService.deleteRequestById(request.getId());
                
                
                ra.addFlashAttribute("messagesucces","le groupe a été ajouté à votre liste avec succès ");
                
                

                return "redirect:/Encadrant/profile";

            } else{
                ra.addFlashAttribute("messagfail","il ya des probleme de trouver cette groupe");
                return "redirect:/Encadrant/profile";
               }
            
         }else if(request.getStatus().equals("suggerer")) {

            List<Subject> subjectsuggerer = subjectService.getSubjectsByCreateur(usersender);
           if(!subjectsuggerer.isEmpty()){
            Subject subjectaccepter = subjectsuggerer.get(0);

            subjectService.updateStatus(subjectaccepter.getId(), "accepter");
            subjectService.updateTimesSelected(subjectaccepter.getId(), subjectaccepter.getTimesSelected()+1);
            
            Request requestanswer = new Request();
                  
                requestanswer.setSeen(false);
                requestanswer.setStatus("accepter");
                requestanswer.setSubject("Votre sujet a été accepeté vous pouvez maintenant Traivailez sur votre  sujet pour votre PFE" );
                requestanswer.setUserSenderId(userid);
                requestanswer.setUserGeterId(etudiantsender.getId());

                requestService.createRequest(requestanswer);

                requestService.deleteRequestById(request.getId());

                ra.addFlashAttribute("messagesucces","votre réponse a été envoyée avec succès ");
                            
            return "redirect:/Encadrant/profile";

                
                  }else{
                    ra.addFlashAttribute("messagfail","il ya des probleme de trouver cette sujet");
                    return "redirect:/Encadrant/profile";

                  }
         }else{


            

            Groupe groupe = groupeService.findById(etudiantsender.getGroupeID()).orElse(null);

            if( groupe != null){

                groupeService.updateGroupeAutorisation(groupe.getId(), true);


                Request requestanswer = new Request();
                  
                requestanswer.setSeen(false);
                requestanswer.setStatus("accepter");
                requestanswer.setSubject("votre demande de prise de rendez-vous a été acceptée" );
                requestanswer.setUserSenderId(userid);
                requestanswer.setUserGeterId(etudiantsender.getId());

                requestService.createRequest(requestanswer);

                requestService.deleteRequestById(request.getId());
                ra.addFlashAttribute("messagesucces","votre réponse a été envoyée avec succès ");
                

              return "redirect:/Encadrant/profile";
            }else{
                ra.addFlashAttribute("messagfail","il ya des probleme to trouver le groube de ce etudiant ");
                return "redirect:/Encadrant/profile";
            }
        
           
            
           
         }
         
    }else{
        ra.addFlashAttribute("messagfail","il ya des probleme to trouver etudiant de cetter groupe");
        return "redirect:/Encadrant/profile";
    }

    }
    else{
         ra.addFlashAttribute("messagfail","il ya des probleme to trouver cette message dans votre boit");
         return "redirect:/Encadrant/profile";
        }
             
    }

    //refuser Request o groupre or suggestion


    @GetMapping("/Encadrant/refuser/{id}")
    public String refuserRequest(@PathVariable("id") Long id, Model model, RedirectAttributes ra
    , HttpSession session){
   
        Long userid = (Long) session.getAttribute("userID");
        Request request = requestService.findById(id).orElse(null);
    if(request !=null){
         User usersender = userService.getUserById(request.getUserSenderId());
            
            Etudiant etudiantsender = etudiantService.getEtudiantById(usersender.getId());
            if(etudiantsender != null)
            {

        if(request.getStatus().equals("demander"))
         {
            

            Groupe groupeaccepter = groupeService.findById(etudiantsender.getGroupeID()).orElse(null);
            if(groupeaccepter !=null){
                groupeService.updateGroupeStatus(groupeaccepter.getId(), "refuser");
                ra.addFlashAttribute("messagesucces","votre réponse a été envoyée avec succès ");
                
                      Request requestanswer = new Request();

                requestanswer.setSeen(false);
                requestanswer.setStatus("refuser");
                requestanswer.setSubject(" l'encadrant sélectionné n'a pas accepté votre demande veuillez sélectionner un nouvel Encadrant" );
                requestanswer.setUserSenderId(userid);
                requestanswer.setUserGeterId(etudiantsender.getId());

                requestService.createRequest(requestanswer);

                requestService.deleteRequestById(request.getId());
                
                ra.addFlashAttribute("messagesucces","votre réponse a été envoyée avec succès ");
                 
                return "redirect:/Encadrant/profile";

            } else{
                ra.addFlashAttribute("messagfail","il ya des probleme de trouver cette groupe");
                return "redirect:/Encadrant/profile";
               }
            
         }else if(request.getStatus().equals("suggerer")){

            List<Subject> subjectsuggerer = subjectService.getSubjectsByCreateur(usersender);
           if(!subjectsuggerer.isEmpty()){
            Subject subjectaccepter = subjectsuggerer.get(0);

            subjectService.updateStatus(subjectaccepter.getId(), "refuser");

            Request requestanswer = new Request();

            requestanswer.setSeen(false);
            requestanswer.setStatus("refuser");
            requestanswer.setSubject(" votre suggestion de sujet a été rejetée veuillez sélectionner ou suggérer un nouveau sujet" );
            requestanswer.setUserSenderId(userid);
            requestanswer.setUserGeterId(etudiantsender.getId());

            requestService.createRequest(requestanswer);

            requestService.deleteRequestById(request.getId());
            ra.addFlashAttribute("messagesucces","votre réponse a été envoyée avec succès ");
                 
            
            return "redirect:/Encadrant/profile";

                
                  }else{
                    ra.addFlashAttribute("messagfail","il ya des probleme de trouver cette sujet");
                    return "redirect:/Encadrant/profile";

                  }
         }else{


            Request requestanswer = new Request();

            requestanswer.setSeen(false);
            requestanswer.setStatus("refuser");
            requestanswer.setSubject(" votre demande de prise de rendez-vous a été rejetée" );
            requestanswer.setUserSenderId(userid);
            requestanswer.setUserGeterId(etudiantsender.getId());

            requestService.createRequest(requestanswer);

            requestService.deleteRequestById(request.getId());

            ra.addFlashAttribute("messagesucces","votre réponse a été envoyée avec succès");
                 
            
            return "redirect:/Encadrant/profile";

         }
         
    }else{
        ra.addFlashAttribute("messagfail","il ya des probleme to trouver etudiant de cetter groupe");
        return "redirect:/Encadrant/profile";
    }

    }
    else{
         ra.addFlashAttribute("messagfail","il ya des probleme to trouver cette message dans votre boit");
         return "redirect:/Encadrant/profile";
        }
             
    }
}
