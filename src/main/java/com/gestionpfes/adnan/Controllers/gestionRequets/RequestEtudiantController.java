package com.gestionpfes.adnan.Controllers.gestionRequets;






import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import com.gestionpfes.adnan.models.Etudiant;
import com.gestionpfes.adnan.models.EtudiantinGroupe;
import com.gestionpfes.adnan.models.Groupe;
import com.gestionpfes.adnan.models.Request;
import com.gestionpfes.adnan.models.User;
import com.gestionpfes.adnan.services.EtudiantService;
import com.gestionpfes.adnan.services.EtudiantinGroupeService;
import com.gestionpfes.adnan.services.GroupeService;
import com.gestionpfes.adnan.services.RequestService;
import com.gestionpfes.adnan.services.UserService;

import ch.qos.logback.core.model.Model;
import jakarta.servlet.http.HttpSession;


@Controller
public class RequestEtudiantController {
    

    @Autowired
    private RequestService requestService;
    
    @Autowired
    private UserService userService;

    @Autowired
    private EtudiantService etudiantService;

     @Autowired
     private GroupeService groupeService;


     @Autowired
     private EtudiantinGroupeService etudiantinGroupesService;







     //mark seen
    
    @GetMapping("/Etudiant/markevu/{id}")
    public String markeseenRequest(@PathVariable("id") Long id, Model model, RedirectAttributes ra){
        for(int i = 0 ; i <7 ; i++){

            System.out.println("i m at the top of profil etudiant controler");
        }

    Request request = requestService.findById(id).orElse(null);
    if(request !=null){

        requestService.updateSeen(id, true);
       
        return "redirect:/Etudiant/profile";

    }
    else{
         ra.addFlashAttribute("messagfail","il ya des probleme to trouver cette message dans votre boit");
         return "redirect:/Etudiant/profile";
        }
           
    }
// delete message

@GetMapping("/Etudiant/deletemsg/{id}")
public String deleteRequest(@PathVariable("id") Long id, Model model, RedirectAttributes ra){
    Request request = requestService.findById(id).orElse(null);
    if(request !=null){
        
            requestService.deleteRequestById(request.getId());
       
        return "redirect:/Etudiant/profile";

    }
    else{
         ra.addFlashAttribute("messagfail","il ya des probleme to trouver cette message dans votre boit");
         return "redirect:/Etudiant/profile";
        }

}


//accepte Request of partener 

@GetMapping("/Etudiant/accepter/{id}")
public String accepteterRequest(@PathVariable("id") Long id, Model model, RedirectAttributes ra
, HttpSession session){

    Long userid = (Long) session.getAttribute("userID");
    Request request = requestService.findById(id).orElse(null);
if(request !=null){
     User usersender = userService.getUserById(request.getUserSenderId());
        
        Etudiant etudiantsender = etudiantService.getEtudiantById(usersender.getId());
        if(etudiantsender != null)
        {

    if(request.getStatus().equals("partenaire"))
     {
        
        User usergeterpartener = userService.getUserById(userid);
        
        Etudiant etudiantgeterpartener = etudiantService.getEtudiantById(usergeterpartener.getId());

        if(etudiantgeterpartener.getGroupeID()!=null){

              requestService.deleteRequestById(request.getId());
            ra.addFlashAttribute("messagfail","vous participez déjà à un groupe pfe");
            return "redirect:/Etudiant/profile";

        }

        Groupe groupepartener = groupeService.findById(etudiantsender.getGroupeID()).orElse(null);
        if(groupepartener !=null){

                etudiantService.updateEtudiantGroupeID(etudiantgeterpartener.getId(), groupepartener.getId());
             EtudiantinGroupe etudiantinGroupe = new EtudiantinGroupe() ;

             etudiantinGroupe.setEtudiantID(etudiantgeterpartener.getId());
             etudiantinGroupe.setGroupeID(groupepartener.getId());

             groupeService.updateGroupeTypeOfWork(groupepartener.getId(), "in Group");
            

             etudiantinGroupesService.createetudiantinGroupe(etudiantinGroupe);


            Request requestanswer = new Request();

            requestanswer.setSeen(false);
            requestanswer.setStatus("accepter");
            requestanswer.setSubject("Mr."+etudiantgeterpartener.getLastname()+" accepté d'être votre partenaire dans votre groupe de PFE" );
            requestanswer.setUserSenderId(etudiantgeterpartener.getId());
            requestanswer.setUserGeterId(etudiantsender.getId());

            requestService.createRequest(requestanswer);

            requestService.deleteRequestById(request.getId());
            
            
            ra.addFlashAttribute("messagesucces","votre réponse a été envoyée avec succès ");
                        
            return "redirect:/Etudiant/profile";

        } else{
            ra.addFlashAttribute("messagfail","il ya des probleme de trouver cette groupe");
            return "redirect:/Etudiant/profile";
           }
        }else{


            requestService.deleteRequestById(request.getId());
            ra.addFlashAttribute("messagfail","vous doit pas reponder à ce message");
            return "redirect:/Etudiant/profile";
        }
        
    }else{
            ra.addFlashAttribute("messagfail","il ya des probleme to trouver etudiant de cetter groupe");
            return "redirect:/Etudiant/profile";
        }
    }
    else{
         ra.addFlashAttribute("messagfail","il ya des probleme to trouver cette message dans votre boit");
         return "redirect:/Etudiant/profile";
        }
             
    }




//refuser Request o groupre or suggestion


@GetMapping("/Etudiant/refuser/{id}")
public String refuserRequest(@PathVariable("id") Long id, Model model, RedirectAttributes ra
, HttpSession session){

    Long userid = (Long) session.getAttribute("userID");
    Request request = requestService.findById(id).orElse(null);
if(request !=null){
    User usersender = userService.getUserById(request.getUserSenderId());
            
    Etudiant etudiantsender = etudiantService.getEtudiantById(usersender.getId());
    if(etudiantsender != null)
    {
        Request requestanswer = new Request();

        requestanswer.setSeen(false);
        requestanswer.setStatus("refuser");
        requestanswer.setSubject(" votre demande de partenaire a été refusée" );
        requestanswer.setUserSenderId(userid);
        requestanswer.setUserGeterId(etudiantsender.getId());

        requestService.createRequest(requestanswer);

        requestService.deleteRequestById(request.getId());

        ra.addFlashAttribute("messagesucces","votre réponse a été envoyée avec succès");
             
        
        return "redirect:/Etudiant/profile";


    }else{
        ra.addFlashAttribute("messagfail","il ya des probleme to trouver etudiant de cetter groupe");
        return "redirect:/Etudiant/profile";
    }
}
else{
     ra.addFlashAttribute("messagfail","il ya des probleme to trouver cette message dans votre boit");
     return "redirect:/Etudiant/profile";
    }


}



}
