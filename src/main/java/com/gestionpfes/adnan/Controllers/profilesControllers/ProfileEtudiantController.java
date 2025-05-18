package com.gestionpfes.adnan.Controllers.profilesControllers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gestionpfes.adnan.DTO.RequestDTO;
import com.gestionpfes.adnan.models.Etudiant;
import com.gestionpfes.adnan.models.Request;
import com.gestionpfes.adnan.models.User;
import com.gestionpfes.adnan.services.EtudiantService;
import com.gestionpfes.adnan.services.RequestService;
import com.gestionpfes.adnan.services.UserService;

import jakarta.servlet.http.HttpSession;



//add list of request here ajouter refuser accepter

@Controller
public class ProfileEtudiantController {
   @Autowired
   EtudiantService etudiantService ;

   @Autowired 
   RequestService requestService ;
   
   @Autowired
   UserService userService;

    @GetMapping("/Etudiant/profile/{userID}")
    public String getProfilepage(@PathVariable Long userID , Model model){
       Etudiant etudiantdata = etudiantService.getEtudiantById(userID);
        model.addAttribute("etudiantdata", etudiantdata);
        
        List<Request> listrequests = requestService.findByUserGeterIdOrderByidDesc(etudiantdata.getId());
        if(!listrequests.isEmpty()){
 
            for(int i = 0 ; i <7 ; i++){
 
                System.out.println("the list isnt empty so the problem is in html shit");
            }
            List <RequestDTO> requestDTOlist = new ArrayList<>();
            for(Request request : listrequests){
 
                User user= userService.getUserById(request.getUserSenderId());
                RequestDTO requestDTO = new RequestDTO();
                requestDTO.setSenderEmail(user.getEmail());
                requestDTO.setSenderID(user.getId());
                requestDTO.setRequestID(request.getId());
                requestDTO.setStatus(request.getStatus());
                requestDTO.setSubject(request.getSubject());
                requestDTO.setSeen(request.isSeen());
                
                requestDTOlist.add(requestDTO);
                 
            }
        model.addAttribute("listrequests", requestDTOlist);
        
    }    
        List<Request> requestseen = requestService.findByUserGeterIdAndSeenOrderByIdDesc(etudiantdata.getId(), false);
 
        if(!requestseen.isEmpty()){
            for(int i = 0 ; i <7 ; i++){
 
                System.out.println("the list isnt empty so the pno he found the notify");
            }
            model.addAttribute("notificate", "vous avez des e-mails pas encore vus");
        }
 

        return"profileEtudiant";
    }

    @GetMapping("/Etudiant/profile")
    public String getProfilepagefrominside( Model model, HttpSession session){
        Long usersessionID = (Long) session.getAttribute("userID");
        Etudiant etudiantdata = etudiantService.getEtudiantById(usersessionID);
        model.addAttribute("etudiantdata", etudiantdata);
        
        List<Request> listrequests = requestService.findByUserGeterIdOrderByidDesc(etudiantdata.getId());
        if(!listrequests.isEmpty()){
 
            for(int i = 0 ; i <7 ; i++){
 
                System.out.println("the list isnt empty so the problem is in html shit");
            }
            List <RequestDTO> requestDTOlist = new ArrayList<>();
            for(Request request : listrequests){
 
                User user= userService.getUserById(request.getUserSenderId());
                RequestDTO requestDTO = new RequestDTO();
                requestDTO.setSenderEmail(user.getEmail());
                requestDTO.setSenderID(user.getId());
                requestDTO.setRequestID(request.getId());
                requestDTO.setStatus(request.getStatus());
                requestDTO.setSubject(request.getSubject());
                requestDTO.setSeen(request.isSeen());
                
                requestDTOlist.add(requestDTO);
                 
            }
        model.addAttribute("listrequests", requestDTOlist);
       
    }    
        List<Request> requestseen = requestService.findByUserGeterIdAndSeenOrderByIdDesc(etudiantdata.getId(), false);
 
        if(!requestseen.isEmpty()){
            for(int i = 0 ; i <7 ; i++){
 
                System.out.println("the list isnt empty so the pno he found the notify");
            }
            model.addAttribute("notificate", "vous avez des e-mails pas encore vus");
        }

        return"profileEtudiant";
    }


    
    @PostMapping("/Etudiant/Modifierdata")
    public String updateData(HttpSession session,@ModelAttribute("etudiantdata") Etudiant etudiantdata, 
    RedirectAttributes ra , Model model){
        Long etudiantid = (Long) session.getAttribute("userID");
        Etudiant etudiantemail =   etudiantService.getEtudiantById(etudiantid) ;
        if(! etudiantemail.getEmail().equals(etudiantdata.getEmail())) {
            Etudiant etudiantcheck = etudiantService.getEtudiantByEmail(etudiantdata.getEmail());
           if(etudiantcheck !=null){
               
               ra.addFlashAttribute("messagfail", "L'email insérer deja existe");
               return "redirect:/Admin/profile/" + etudiantid;
           }
        }   
        etudiantService.updateEtudiantinfo(etudiantid,etudiantdata);
         ra.addFlashAttribute("etudiantdata", etudiantdata);
        ra.addFlashAttribute("messagesucces", "Votre données a été mis à jour avec succès.");

        return "redirect:/Etudiant/profile";
    }

    @PostMapping("/Etudiant/Modifierpasseword")
    public String updatepassword(HttpSession session,
    RedirectAttributes ra ,@RequestParam("oldpassword") String oldpassword,
    @RequestParam("newpassword") String newpassword , @RequestParam("confipassword") String confipassword
    , Model model){
        Long etudiantid = (Long) session.getAttribute("userID");
        Etudiant etudiantcheck = etudiantService.getEtudiantById(etudiantid);
        if( !etudiantcheck.getPassword().equals(oldpassword)){
            ra.addFlashAttribute("messagfail", "mot de passe incorrect.");
            return "redirect:/Etudiant/profile";
        }else if(!newpassword.equals(confipassword)){
            ra.addFlashAttribute("messagfail", "le mot de passe n'est pas identique.");
            return "redirect:/Etudiant/profile";
        }else{
            etudiantcheck = etudiantService.updateEtudiantPassword(etudiantid,newpassword);
         
         ra.addFlashAttribute("etudiantdata", etudiantcheck);
        ra.addFlashAttribute("messagesucces", "le mot de passe a été mis à jour avec succès.");
        return "redirect:/Etudiant/profile";
        }
        
    }
    @PostMapping("/Etudiant/ModifierImage")
public String modifierImage(@RequestParam("image") MultipartFile image, Model model 
, RedirectAttributes red , HttpSession session) {
    try {
        // Check if the uploaded file is a valid image file
        String fileType = image.getContentType();
        if (!fileType.equals("image/png") && !fileType.equals("image/jpeg")) {
            red.addFlashAttribute("messagfail", "Le fichier doit être de type PNG ou JPG.");
            return "redirect:/Etudiant/profile";
        }

        Long usercheckid = (Long) session.getAttribute("userID");  
        // Get the encadrant object
        Etudiant etudiant = etudiantService.getEtudiantById(usercheckid);
       
        // Delete the old image file
        if(!etudiant.getImage().equals("/images/annonyme.jpg")){
        if (etudiant.getImage() != null) {
            Path oldImagePath = Paths.get("src/main/resources/static" + etudiant.getImage());
            Files.deleteIfExists(oldImagePath);
                 }

                }
        // Generate a unique filename for the uploaded image
        String extension = "." + fileType.split("/")[1];
        String filename = UUID.randomUUID().toString() + extension;

        // Copy the uploaded image to the Images folder in your static folder
        Path imagePath = Paths.get("src/main/resources/static/images/" + filename);
        Files.copy(image.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);

        // Update the encadrant object with the new image path
        etudiant.setImage("/images/" + filename);

        // Save the updated encadrant object
        etudiantService.save(etudiant);

        // Redirect to the profileencadrant page
        return "redirect:/Etudiant/profile";
    } catch (Exception e) {
        model.addAttribute("messagfail", "Une erreur s'est produite lors de l'enregistrement de l'image.");
        return  "redirect:/Etudiant/profile";
    }
}

    
}
