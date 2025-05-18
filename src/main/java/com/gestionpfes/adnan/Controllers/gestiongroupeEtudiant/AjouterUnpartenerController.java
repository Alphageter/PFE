package com.gestionpfes.adnan.Controllers.gestiongroupeEtudiant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gestionpfes.adnan.models.Etudiant;
import com.gestionpfes.adnan.models.EtudiantinGroupe;
import com.gestionpfes.adnan.models.Request;
import com.gestionpfes.adnan.services.EtudiantService;
import com.gestionpfes.adnan.services.EtudiantinGroupeService;
import com.gestionpfes.adnan.services.RequestService;

import jakarta.servlet.http.HttpSession;

@Controller
public class AjouterUnpartenerController {
    
    @Autowired
    private EtudiantService etudiantService;

    @Autowired
    private EtudiantinGroupeService etudiantinGroupeService;

    @Autowired
    private RequestService requestService;

@GetMapping("/Etudiant/addPartener")
public String displayaddpartenairpage(Model model){

    return "addEtudiantGroupe";
}

    @PostMapping("/Etudiant/addEtudiantinGroupe")
    public String addEtudiantinGroupe(  
                                         @RequestParam(required = false) String email,
                                         Model model , RedirectAttributes re,
                                         HttpSession session  ){
                 Long userid = (Long) session.getAttribute("userID");

                 for(int i = 0 ; i<7 ; i++){
                    System.out.println("im at the tope of /Etudiant/Groupe the user id is    !!!! "+userid);
                }
                 Etudiant etudiantsender = etudiantService.getEtudiantById(userid);
                 for(int i = 0 ; i<7 ; i++){
                    System.out.println("i got the etudiant    !!!! ");
                }
                 Etudiant etudiant = etudiantService.getEtudiantByEmail(email);
                 for(int i = 0 ; i<7 ; i++){
                    System.out.println("i got the  etudiantpartener by email    !!!! ");
                }
                 if(etudiant==null){
    
                    model.addAttribute("messagfail", "étudiant que vous insérez n'a pas été trouvé");
                    
                    return "addEtudiantGroupe";
                 }else{
                    EtudiantinGroupe etudiantinGroupe = etudiantinGroupeService.getetudiantinGroupesByetudiantID(etudiant.getId());
                    if(etudiantinGroupe != null){
    
                        model.addAttribute("messagfail", "l'étudiant que vous insérez a déjà un groupe :"+etudiant.getEmail());
                        
                        return "addEtudiantGroupe";
    
                    }else if(!etudiantsender.getFilier().equals(etudiant.getFilier())){

                        model.addAttribute("messagfail", "l'étudiant que vous insérez Il n'appartient pas à la même Filier : "+etudiantsender.getFilier());
                        
                        return "addEtudiantGroupe";

                    }
                    else{
                         // we must add a request here
  
                        Request requestpartener = new Request();
                        requestpartener.setSeen(false);
                        requestpartener.setStatus("partenaire");
                        requestpartener.setSubject("M."+etudiantsender.getLastname() +" vous demande de rejoindre son groupe PFE" );
                        requestpartener.setUserSenderId(userid);
                        requestpartener.setUserGeterId(etudiant.getId());

                   
                        requestService.createRequest(requestpartener);
                        
                        re.addFlashAttribute("messagesucces", "la demande a été envoyée avec succès");
                  
                        return "redirect:/Etudiant/Groupe";
                    }
                }
    
                                         }


}
