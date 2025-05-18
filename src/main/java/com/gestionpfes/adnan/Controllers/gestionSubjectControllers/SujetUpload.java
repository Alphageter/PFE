package com.gestionpfes.adnan.Controllers.gestionSubjectControllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gestionpfes.adnan.handleErrores.SubjectNotFoundException;
import com.gestionpfes.adnan.models.Encadrant;
import com.gestionpfes.adnan.models.Subject;
import com.gestionpfes.adnan.models.User;
import com.gestionpfes.adnan.services.EncadrantService;
import com.gestionpfes.adnan.services.SubjectService;
import com.gestionpfes.adnan.services.UserService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


@Controller
public class SujetUpload {
    private static final Logger logger = LogManager.getLogger(SujetUpload.class);

    @Autowired
    SubjectService subjectService;

    @Autowired
    UserService userService;

    @Autowired
    EncadrantService encadrantService ;
    

    //DISPLAY LIST of SUJETS ---------------
    @GetMapping("/Encadrant/Sujets")
    public String showSubjects(Model model , HttpSession session) {
        Encadrant encadrant = encadrantService.getEncadrantById((Long)session.getAttribute("userID"));
        List<Subject> subjects = subjectService.getSubjectsByCreateur(encadrant);
        model.addAttribute("listsujets", subjects);
        return "listSujet";

    }
     //DISPLAY new subject form  ---------------

    @GetMapping("/Encadrant/newsujet")
    public String newSubject(Model model, HttpSession session) {
      model.addAttribute("sujet", new Subject());
      Encadrant encadrant = encadrantService.getEncadrantById((Long)session.getAttribute("userID"));
    if(encadrant.getFilier().equals("SMI")){
        model.addAttribute("SMI", 1);
    }else{
        model.addAttribute("SMI", 2);
    }

        return "newSujet";
    }

    //DOWNLOAD the pdf file 
    @GetMapping("/Encadrant/download/subject/{subjectId}")
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


  // Create new sujet ------------------

    @PostMapping("/Encadrant/savesujet")
              public String createSubject(@RequestParam("name") String name,
                             @RequestParam("language") String language,
                             @RequestParam("file") MultipartFile file,
                             HttpSession session ,
                             RedirectAttributes re,
                             Model model)  {

   try { // Check if the uploaded file is a PDF file 
    if (!file.getContentType().equals("application/pdf")) { 
       model.addAttribute("messagfail", "Le fichier sélectionné n'est pas un PDF");
      return "newSujet"; 
    }
           Subject subject = new Subject();
           subject.setName(name);
           subject.setLanguage(language);
           subject.setTimesSelected(0);
           subject.setStatus("accepter");
            // or use an empty list
    
           // Get the ID of the currently logged-in user from the HttpSession
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
     return "redirect:/Encadrant/Sujets";

} catch (IOException e) 
{
  e.printStackTrace();
  model.addAttribute("messagfail", "une erreur s'est produite lors du téléchargement du fichier.");
  return "newSujet"; 
  }
}



@GetMapping("/Encadrant/editSujet/{id}")
public String editesujetbyid(@PathVariable("id") Long id, Model model, RedirectAttributes ra , HttpSession session) {

  try{
    Subject subject = subjectService.getSubjectById(id);
    Encadrant encadrant = encadrantService.getEncadrantById((Long)session.getAttribute("userID"));
    if(encadrant.getFilier().equals("SMI")){
        model.addAttribute("SMI", 1);
    }else{
        model.addAttribute("SMI", 2);
    }
    model.addAttribute("subject", subject);
      return "updateSujet";
 
  }
  catch (SubjectNotFoundException e) {
    ra.addFlashAttribute("messagefail", e.getMessage());
    return "redirect:/Encadrant/Sujets";
}
}


@PostMapping("/Encadrant/editSujet/{subjectId}")
public String editSubject(@PathVariable Long subjectId,
                           @RequestParam("name") String name,
                           @RequestParam("language") String language,
                           @RequestParam(value = "pdfFile", required = false) MultipartFile pdfFile,
                           HttpSession session, RedirectAttributes redirectAttributes) throws IOException {

    try {
        Long userid = (Long) session.getAttribute("userID");
        User userlogedin = userService.getUserById(userid);
        Subject subject = subjectService.getSubjectById(subjectId);

        if (subject == null) {
            redirectAttributes.addFlashAttribute("messagfail", "Sujet introuvable");
            return "redirect:/Encadrant/Sujets";
        }

        // Parameter validation
        if (name == null || name.isEmpty()) {
            redirectAttributes.addFlashAttribute("messagfail", "Le nom du sujet est obligatoire");
            return "redirect:/Encadrant/editSujet/" + subjectId;
        }

        if (language == null || language.isEmpty()) {
            redirectAttributes.addFlashAttribute("messagfail", "La langue est obligatoire");
            return "redirect:/Encadrant/editSujet/" + subjectId;
        }
        
        if (!pdfFile.isEmpty()) {
            if (!pdfFile.getContentType().equals("application/pdf")) {
                redirectAttributes.addFlashAttribute("messagfail", "Le fichier sélectionné n'est pas un PDF");
               
                return "redirect:/Encadrant/editSujet/" + subjectId;
            }

            String oldPdfFile = subject.getPdfFile();
            if (oldPdfFile != null && !oldPdfFile.isEmpty()) {
            
                File file = new File(oldPdfFile);
                if (file.exists()) {
                     file.delete(); 
                }
            }

             // Generate a unique filename for the uploaded image
              String fileType = pdfFile.getContentType();
              String extension = "." + fileType.split("/")[1];
              String filename = UUID.randomUUID().toString() + extension;
    
           // Save the file to the static folder of the project
        
           // String fileName = StringUtils.cleanPath(Objects.requireNonNull(pdfFile.getOriginalFilename()));
            Path path = Paths.get("src/main/resources/static/files/" + filename);
            Files.copy(pdfFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING); 
            subject.setPdfFile(path.toString());
        }
 
        subject.setName(name);
        subject.setLanguage(language);
        subject.setCreateur(userlogedin);
        

        subjectService.createSubject(subject);

        redirectAttributes.addFlashAttribute("messagesucces", "Sujet mis à jour avec succès");
        return "redirect:/Encadrant/Sujets";
    } catch (Exception e) {
        

                logger.error("Error occurred during subject update", e);
                e.printStackTrace();
        redirectAttributes.addFlashAttribute("messagfail", "Erreur lors de la mise à jour du sujet");
        return "redirect:/Encadrant/editSujet/" + subjectId;
    }
}


 
//DELTE----------------------------

@GetMapping("/Encadrant/deleteSujet/{id}")
public String deleteUser(@PathVariable("id") Long id, RedirectAttributes ra) {
    try {
        Subject subject = subjectService.getSubjectById(id);
        String status = subject.getStatus();
        String oldPdfFile = subject.getPdfFile();
        if (oldPdfFile != null && !oldPdfFile.isEmpty()) {
        
            File file = new File(oldPdfFile);
            if (file.exists()) {
                 file.delete(); 
            }
        }

        subjectService.deleteSubjectById(id);
        if(status.equals("accepter")){
        ra.addFlashAttribute("messagesucces", "Le sujet  ID " + id + " a été supprimé..");
        return "redirect:/Encadrant/Sujets";
    }else{
        ra.addFlashAttribute("messagesucces", "Le sujet  ID " + id + " a été supprimé..");
        return "redirect:/Encadrant/resources";
         }

    } catch (SubjectNotFoundException e) {
        ra.addFlashAttribute("messagefail", e.getMessage());
    }
    return "redirect:/Encadrant/resources";
}

//SEARCH ----------------------

@PostMapping("/Encadrant/searchsujet")
public String searchSujet(@RequestParam("search") String search, Model model , RedirectAttributes red) {
    List<Subject> sujetList = new ArrayList<>();
    
    try {
        // Search by ID
        if (StringUtils.hasText(search)) { 
          if (NumberUtils.isParsable(search)) {
            Subject subject = subjectService.getSubjectById(Long.parseLong(search));
           
            if (subject != null) {
                sujetList.add(subject);
            }
        } else {
            // Search by name or language
            sujetList = subjectService.searchSubject(search);
        }

        // If no results found
        if (sujetList.isEmpty()) {
          red.addFlashAttribute("messagfail", "Aucun sujet trouvé.");
            return "redirect:/Encadrant/Sujets";
        } else {
            model.addAttribute("listsujets", sujetList);
            return "listSujet";
        }
      }else{
        red.addFlashAttribute("messagfail", "la recherche ne peut pas être vide");
            return "redirect:/Encadrant/Sujets";
      }
    } catch (Exception e) {
        model.addAttribute("messagfail", "Une erreur est survenue lors de la recherche des sujets.");
        return "redirect:/Encadrant/Sujets";
    }
}
}
