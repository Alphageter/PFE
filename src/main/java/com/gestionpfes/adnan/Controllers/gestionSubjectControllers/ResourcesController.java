package com.gestionpfes.adnan.Controllers.gestionSubjectControllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gestionpfes.adnan.models.Encadrant;
import com.gestionpfes.adnan.models.Subject;
import com.gestionpfes.adnan.models.User;
import com.gestionpfes.adnan.services.EncadrantService;
import com.gestionpfes.adnan.services.SubjectService;
import com.gestionpfes.adnan.services.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ResourcesController {


    @Autowired
    private SubjectService subjectService;

    @Autowired
    private UserService userService;

    @Autowired
    private EncadrantService encadrantService ;



    //DISPLAY LIST of SUJETS ---------------
    @GetMapping("/Encadrant/resources")
    public String showSubjects(Model model , HttpSession session) {
        User user = userService.getUserById((Long)session.getAttribute("userID"));
        List<Subject> listdocuments = subjectService.getSubjectsByCreateurAndStatus(user, "document");
        if(!listdocuments.isEmpty()){
            model.addAttribute("listdocuments", listdocuments);
         }
        
         List<Subject> listlines = subjectService.getSubjectsByCreateurAndStatus(user, "line");
         if(!listlines.isEmpty()){
          for(int i = 0;i<6;i++){
            System.out.println("im in listlines not empty");
          }
            model.addAttribute("listlines", listlines);
             }

        return "listResource";

    }
     
    //DISPLAY new resources form  ---------------

    @GetMapping("/Encadrant/newresource")
    public String newSubject(Model model, HttpSession session) {
      model.addAttribute("sujet", new Subject());
      Encadrant encadrant = encadrantService.getEncadrantById((Long)session.getAttribute("userID"));
    if(encadrant.getFilier().equals("SMI")){
        model.addAttribute("SMI", 1);
    }else{
        model.addAttribute("SMI", 2);
    }

        return "newResource";
    }


    //AJouter un nouveau document ressource

    @PostMapping("/Encadrant/saveresource")
              public String createSubject(@RequestParam("name") String name,
                             @RequestParam("language") String language,
                             @RequestParam("file") MultipartFile file,
                             HttpSession session ,
                             RedirectAttributes re,
                             Model model)  {

 try { // Check if the uploaded file is a PDF file 
    if (!file.getContentType().equals("application/pdf")) { 
       model.addAttribute("messagfail", "Le fichier sélectionné n'est pas un PDF");
      return "newResource"; 
    }
           Subject subject = new Subject();
           subject.setName(name);
           subject.setLanguage(language);
           subject.setTimesSelected(0);
           subject.setStatus("document");
           
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

       re.addFlashAttribute("messagesucces", "le sujet a été créé avec succès");
     return "redirect:/Encadrant/resources";

    } catch (IOException e) 
    {
      e.printStackTrace();
      model.addAttribute("messagfail", "une erreur s'est produite lors du téléchargement du fichier.");
      return "newResource"; 
      }
                                
                             }

    //ajouter un line 

    @PostMapping("/Encadrant/savelink")
    public String createSubject(@RequestParam("name") String name,
                   @RequestParam("line") String line,
                   HttpSession session ,
                   RedirectAttributes re,
                   Model model)  {


 Subject subject = new Subject();
 subject.setName(name);
 subject.setLanguage("none");
 subject.setTimesSelected(0);
 subject.setStatus("line");
 
 Long userId = (Long) session.getAttribute("userID");
 User createur = userService.getUserById(userId);
 subject.setCreateur(createur);

 subject.setPdfFile(line);

 subjectService.createSubject(subject);

 re.addFlashAttribute("messagesucces", "le line a été créé avec succès");
 return "redirect:/Encadrant/resources";


                   }


}
