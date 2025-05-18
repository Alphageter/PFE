package com.gestionpfes.adnan.Controllers.gestionraportandresource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gestionpfes.adnan.handleErrores.SubjectNotFoundException;
import com.gestionpfes.adnan.models.Etudiant;
import com.gestionpfes.adnan.models.Groupe;
import com.gestionpfes.adnan.models.Request;
import com.gestionpfes.adnan.models.Subject;
import com.gestionpfes.adnan.models.User;
import com.gestionpfes.adnan.services.EtudiantService;
import com.gestionpfes.adnan.services.GroupeService;
import com.gestionpfes.adnan.services.RequestService;
import com.gestionpfes.adnan.services.SubjectService;
import com.gestionpfes.adnan.services.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class EtudiantRaportController {

    @Autowired
    private EtudiantService etudiantService;

    @Autowired
    private GroupeService groupeService;

    @Autowired
    private UserService userService;
    
    @Autowired
    private SubjectService subjectService;

    @Autowired
    private RequestService requestService;


    @GetMapping("/Etudiant/raportpfe")
    public String raportpfe(Model model ){

        return "newraportEtudiant";
    }


    @PostMapping("/Encadrant/saveraportetudiant")
    public String sveraport(@RequestParam("name") String name,
                           @RequestParam("file") MultipartFile file,
                            Model model , HttpSession session,
                            RedirectAttributes re){
       
        Etudiant etudiant = etudiantService.getEtudiantById((Long)session.getAttribute("userID"));

        Groupe groupe = groupeService.findById(etudiant.getGroupeID()).orElse(null);
        
         if(groupe!=null){

            try { // Check if the uploaded file is a PDF file 
                if (!file.getContentType().equals("application/pdf")) { 
                   model.addAttribute("messagfail", "Le fichier sélectionné n'est pas un PDF");
                  return "newResource"; 
                }

                //create the raport and save it 
                       Subject subject = new Subject();
                       subject.setName(name);
                       subject.setLanguage("non");
                       subject.setTimesSelected(0);
                       subject.setStatus("raport");
                       
                       Long userId = (Long) session.getAttribute("userID");
                       User createur = userService.getUserById(userId);
                       subject.setCreateur(createur);
            
                       // Generate a unique filename for the uploaded image
                    String fileType = file.getContentType();
                    String extension = "." + fileType.split("/")[1];
                    String filename = UUID.randomUUID().toString() + extension;
            
                    // Save the file to the static folder of the project
                    //   String fileName = StringUtils.cleanPath(file.getOriginalFilename());
                    Path path = Paths.get("src/main/resources/static/files/" + filename);
                    Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                    subject.setPdfFile(path.toString());
              
                   subjectService.createSubject(subject);
                   
                   //create the message for the encadrant 
                  

                   Request requestraport = new Request();
                   requestraport.setSubject("raport");
                   requestraport.setSubject("MR."+etudiant.getEmail() +" a mis en ligne un "+
                   "nouveau rapport sur son projet de fin d'études pour le groupe : "+groupe.getName());
                   requestraport.setSeen(false);
                   requestraport.setUserSenderId(etudiant.getId());
                   requestraport.setUserGeterId(groupe.getEndarantID());

                   requestService.createRequest(requestraport);
            
                   re.addFlashAttribute("messagesucces", "votre raport a été envoyer avec succès");
                 return "redirect:/Etudiant/Groupe";
            
                } catch (IOException e) 
                {
                  e.printStackTrace();
                  model.addAttribute("messagfail", "une erreur s'est produite lors du téléchargement du fichier.");
                  return "newraportEtudiant"; 
                  }
        
         }else{

            model.addAttribute("messagfail","il y a un problème pour trouver votre groupe");
            return "newraportEtudiant";
         }

    }

    @GetMapping("/Etudiant/deleteSujet/{id}")
public String deleteUser(@PathVariable("id") Long id, RedirectAttributes ra) {
    try {
        Subject subject = subjectService.getSubjectById(id);
        
        String oldPdfFile = subject.getPdfFile();
        if (oldPdfFile != null && !oldPdfFile.isEmpty()) {
        
            File file = new File(oldPdfFile);
            if (file.exists()) {
                 file.delete(); 
            }
        }

        subjectService.deleteSubjectById(id);
       
        ra.addFlashAttribute("messagesucces", "Votre raport  ID " + id + " a été supprimé..");
        

    } catch (SubjectNotFoundException e) {
        ra.addFlashAttribute("messagefail", e.getMessage());
    }
    return "redirect:/Etudiant/Groupe";
}



}
