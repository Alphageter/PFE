package com.gestionpfes.adnan.Controllers.gestiongroupeEtudiant;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
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


import jakarta.servlet.http.HttpSession;

@Controller
public class UploadSujetEtudiant {


    
    @Autowired
    SubjectService subjectService;

    @Autowired
    UserService userService;

    @Autowired
    EncadrantService encadrantService ;

    @Autowired
    EtudiantService etudiantService;

    @Autowired
    GroupeService groupeService;

    @Autowired
    RequestService requestService;
//add Request here

             // Create new sujet ------------------

    @PostMapping("/Etudiant/savesujet")
    public String createSubject(@RequestParam("name") String name,
                   @RequestParam("language") String language,
                   @RequestParam("file") MultipartFile file,
                   HttpSession session ,
                   RedirectAttributes re,
                   Model model)  {

try { 
    Long userId = (Long) session.getAttribute("userID");
    User createur = userService.getUserById(userId);

    List<Subject> listsubjectdelete = subjectService.getSubjectByCreateurOrderByid(createur);
    if(!listsubjectdelete.isEmpty()){
    Subject subjectdelet = listsubjectdelete.get(0);
      if (subjectdelet != null) {
    Path oldPdfFIlePath = Paths.get( subjectdelet.getPdfFile());
    Files.deleteIfExists(oldPdfFIlePath);
       subjectService.deleteSubjectById(subjectdelet.getId()); 
               }
    }
    for(int i = 0 ; i<7 ; i++){
        System.out.println("im in upload subject controler ");
    }
    // Check if the uploaded file is a PDF file 
if (!file.getContentType().equals("application/pdf")) { 
    for(int i = 0 ; i<7 ; i++){
        System.out.println("is not pdf ???? ");
    }
model.addAttribute("messagfail", "Le fichier sélectionné n'est pas un PDF");
return "newSujet"; 
}
 Subject subject = new Subject();
 subject.setName(name);
 subject.setLanguage(language);
 subject.setTimesSelected(0);
 subject.setStatus("en attente");
  // or use an empty list

  

 // Get the ID of the currently logged-in user from the HttpSession

 subject.setCreateur(createur);
 for(int i = 0 ; i<7 ; i++){
    System.out.println("we see latert ???? "+createur.getId());
}
// +createur.getEmail()

 // Generate a unique filename for the uploaded image
String fileType = file.getContentType();
String extension = "." + fileType.split("/")[1];
String filename = UUID.randomUUID().toString() + extension;

 // Save the file to the static folder of the project
//   String fileName = StringUtils.cleanPath(file.getOriginalFilename());
Path path = Paths.get("src/main/resources/static/files/" + filename);
Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
subject.setPdfFile(path.toString());
for(int i = 0 ; i<7 ; i++){
    System.out.println("mosiibba 3awedd  !!!"+filename);
}
subjectService.createSubject(subject);
for(int i = 0 ; i<7 ; i++){
    System.out.println("we must creat the subject ???? ");
}
Etudiant etudiantupdate = etudiantService.getEtudiantById(createur.getId());
Groupe groupe = groupeService.findById(etudiantupdate.getGroupeID()).orElse(null);
if(groupe != null){
    groupeService.updateGroupeSubjectID(groupe.getId(), subject.getId());
    for(int i = 0 ; i<7 ; i++){
        System.out.println("im updated subjectID in groupe!!!!");
    }
}

for(int i = 0 ; i<7 ; i++){
    System.out.println("just i m hereer ???? ");
}

Etudiant etudiantsugest = etudiantService.getEtudiantById(userId);

Groupe groupetarget = groupeService.findById(etudiantsugest.getGroupeID()).orElse(null);
if(groupetarget!=null)
{
    Encadrant encadrant = encadrantService.getEncadrantById(groupetarget.getEndarantID());

    Request requestsuggerer  = new Request();
    requestsuggerer.setSeen(false);
    requestsuggerer.setStatus("suggerer");
    requestsuggerer.setSubject("M."+etudiantupdate.getLastname() +"Suggérez un sujet pour le PFE"
     +"que vous pouvez examiner à travers les informations de groupe : "+groupe.getName() );
    requestsuggerer.setUserSenderId(etudiantupdate.getId());
    requestsuggerer.setUserGeterId(encadrant.getId());

    requestService.createRequest(requestsuggerer);

}
else{
    for(int i = 0 ; i<7 ; i++){
        System.out.println("mosiibba 3awedd  !!!");
    }

}


                   
                    
         for(int i = 0 ; i<7 ; i++){
            System.out.println("im uploaded subject sucessfullybject!!!!");
        }
//re.addFlashAttribute("messagesucces", "le sujet a été envoyé à l'encadrant avec succès");
return "redirect:/Etudiant/Groupe";

} catch (IOException e) 
{
    for(int i = 0 ; i<7 ; i++){
        System.out.println("im in catch subject uplosd found so we work on subject!!!!");
    }
e.printStackTrace();
model.addAttribute("messagfail", "une erreur s'est produite lors du téléchargement du fichier.");
Long userid = (Long) session.getAttribute("userID");

Etudiant etudiant = etudiantService.getEtudiantById(userid);

Optional<Groupe> optionalgroupe = groupeService.findById(etudiant.getGroupeID());

Groupe groupe = optionalgroupe.get();

Encadrant encadrant = encadrantService.getEncadrantById(groupe.getEndarantID());
 
List<Subject> subjects = subjectService.getSubjectByUsertimesSelectedLessThanEqual(encadrant, 2);

model.addAttribute("subjects",subjects);

return "GroupeEtudiant"; 
}
}








}
