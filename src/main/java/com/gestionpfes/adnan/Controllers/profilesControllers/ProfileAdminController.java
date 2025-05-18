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
import com.gestionpfes.adnan.models.Admin;
import com.gestionpfes.adnan.models.Request;
import com.gestionpfes.adnan.models.User;
import com.gestionpfes.adnan.services.AdminService;
import com.gestionpfes.adnan.services.RequestService;
import com.gestionpfes.adnan.services.UserService;

import jakarta.servlet.http.HttpSession;


//add list of request here ajouter refuser accepter

@Controller
public class ProfileAdminController {
   @Autowired
   AdminService adminService ;

   @Autowired 
   RequestService requestService ;

   @Autowired
   UserService userService;

    @GetMapping("/Admin/profile/{userID}")
    public String getProfilepage(@PathVariable Long userID , Model model){
       Admin admindata = adminService.getAdminById(userID);
        model.addAttribute("admindata", admindata);

        List<Request> listrequests = requestService.findByUserGeterIdOrderByidDesc(admindata.getId());
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
       
        List<Request> requestseen = requestService.findByUserGeterIdAndSeenOrderByIdDesc(admindata.getId(), false);

        if(!requestseen.isEmpty()){
            for(int i = 0 ; i <7 ; i++){

                System.out.println("the list isnt empty so the pno he found the notify");
            }
             
          
            model.addAttribute("notificate", "vous avez des e-mails pas encore vus");
        }
        

        return"profileAdmin";
    }
    @GetMapping("/Admin/profile")
    public String getProfilepagefrominside( Model model, HttpSession session){
        Long usersessionID = (Long) session.getAttribute("userID");
       Admin admindata = adminService.getAdminById(usersessionID);
        model.addAttribute("admindata", admindata);
        for(int i = 0 ; i <7 ; i++){

            System.out.println("i m at the top of profil admin controler");
        }
        List<Request> listrequests = requestService.findByUserGeterIdOrderByidDesc(admindata.getId());
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
       
        List<Request> requestseen = requestService.findByUserGeterIdAndSeenOrderByIdDesc(admindata.getId(), false);

        if(!requestseen.isEmpty()){
            for(int i = 0 ; i <7 ; i++){

                System.out.println("the list isnt empty so the pno he found the notify");
            }
            model.addAttribute("notification", "there is some new messages");
        }
        

        return"profileAdmin";
    }

 

    @PostMapping("/Admin/Modifierdata")
    public String updateData(@ModelAttribute("admindata") Admin admindata, 
    RedirectAttributes ra , Model model,HttpSession session){
      
        
            Long usercheckid = (Long) session.getAttribute("userID"); 
         Admin adminemail =   adminService.getAdminById(usercheckid) ;
         if(! adminemail.getEmail().equals(admindata.getEmail())) {
            Admin admincheck = adminService.getAdminByEmail(admindata.getEmail());
            if(admincheck !=null){
                
                ra.addFlashAttribute("messagfail", "L'email insérer deja existe");
                return "redirect:/Admin/profile/" + usercheckid;
            }

         }     
         adminService.updateAdminInfo(usercheckid,admindata);
         ra.addFlashAttribute("admindata", admindata);
         ra.addFlashAttribute("messagesucces", "Votre données a été mis à jour avec succès.");

        return "redirect:/Admin/profile/" + usercheckid;
    }

    @PostMapping("/Admin/Modifierpasseword")
    public String updatepassword(HttpSession session ,
    RedirectAttributes ra ,@RequestParam("oldpassword") String oldpassword,
    @RequestParam("newpassword") String newpassword , @RequestParam("confipassword") String confipassword
    , Model model){
        Long adminid = (Long) session.getAttribute("userID");
        Admin admincheck = adminService.getAdminById(adminid);
        if( !admincheck.getPassword().equals(oldpassword)){
            ra.addFlashAttribute("messagfail", "mot de passe incorrect.");
            return "redirect:/Admin/profile";
        }else if(!newpassword.equals(confipassword)){
            ra.addFlashAttribute("messagfail", "le mot de passe n'est pas identique.");
            return "redirect:/Admin/profile";
        }else{
            admincheck = adminService.updateAdminPassword(adminid,newpassword);
        ra.addFlashAttribute("admindata", admincheck);
        ra.addFlashAttribute("messagesucces", "le mot de passe a été mis à jour avec succès.");
        return "redirect:/Admin/profile";
        }
        
    }

    @PostMapping("/Admin/ModifierImage")
public String modifierImage(@RequestParam("image") MultipartFile image, Model model 
, RedirectAttributes red , HttpSession session) {
    try {
        // Check if the uploaded file is a valid image file
        String fileType = image.getContentType();
        if (!fileType.equals("image/png") && !fileType.equals("image/jpeg")) {
            red.addFlashAttribute("messagfail", "Le fichier doit être de type PNG ou JPG.");
            return "redirect:/Admin/profile";
        }

        Long usercheckid = (Long) session.getAttribute("userID");  
        // Get the admin object
        Admin admin = adminService.getAdminById(usercheckid);

       // Delete the old image file
       if(!admin.getImage().equals("/images/annonyme.jpg")){
       if (admin.getImage() != null) {
        Path oldImagePath = Paths.get("src/main/resources/static" + admin.getImage());
        Files.deleteIfExists(oldImagePath);
             }

            }

        // Generate a unique filename for the uploaded image
        String extension = "." + fileType.split("/")[1];
        String filename = UUID.randomUUID().toString() + extension;

        // Copy the uploaded image to the Images folder in your static folder
        Path imagePath = Paths.get("src/main/resources/static/images/" + filename);
        Files.copy(image.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);

        // Update the admin object with the new image path
        admin.setImage("/images/" + filename);

        // Save the updated admin object
        adminService.save(admin);

        // Redirect to the profileAdmin page
        return "redirect:/Admin/profile";
    } catch (Exception e) {
        model.addAttribute("messagfail", "Une erreur s'est produite lors de l'enregistrement de l'image.");
        return  "redirect:/Admin/profile";
    }
}

    


    
}
