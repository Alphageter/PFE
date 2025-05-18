package com.gestionpfes.adnan.Controllers.gestionReports;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gestionpfes.adnan.models.Request;
import com.gestionpfes.adnan.services.RequestService;
import jakarta.servlet.http.HttpSession;



@Controller
public class ReportEtudiantController {
    

 

@Autowired
private RequestService requestService;



@GetMapping("/Etudiant/showreportform")
public String reportEncadrant(Model model , HttpSession session ){

return "ReportEtudiant";


}
  


@PostMapping("/Etudiant/Report")
public String reportEncadrant(Model model , HttpSession session ,  @RequestParam(required = false) String report){
  
    Long userid = (Long) session.getAttribute("userID");
if(report != null){
     Request requestrepoRequest = new Request();

     requestrepoRequest.setSeen(false);
     requestrepoRequest.setStatus("report");
     requestrepoRequest.setSubject(report );
     requestrepoRequest.setUserSenderId(userid);
     requestrepoRequest.setUserGeterId(1L);

     requestService.createRequest(requestrepoRequest);


     model.addAttribute("messagesucces", "nous avons reçu votre rapport avec succès");


    return "ReportEtudiant";
}else{

    model.addAttribute("maessagfail", "Merci de décrire votre problème");


    return "ReportEtudiant";
}

}



}
