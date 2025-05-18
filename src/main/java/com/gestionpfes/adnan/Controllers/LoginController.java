package com.gestionpfes.adnan.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gestionpfes.adnan.models.User;
import com.gestionpfes.adnan.services.UserService;

import jakarta.servlet.http.HttpSession;

import org.springframework.ui.Model;

@Controller
public class LoginController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/validation")
    public String valider() {
        return "valider";
    }
    
    @GetMapping("/login")
	public String login(Model model, @RequestParam(name="success" , required = false) String success) {
		if(success!=null){
            model.addAttribute("messagesucces", "le compte a été validé avec succès");
        }
        return "loginpfe";
	}
    

    @PostMapping("/login/check")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password,
     HttpSession session,Model model) {
        
       
        // Find user by email and password
        User user = userService.getUserByEmail(username);
        for(int i =0 ;i<7;i++){
            System.out.println("just to see the email !!!!!!!! "+username);
        }
        if (user == null) {
            // Account doesn't exist
            
            model.addAttribute("errorMessage", "Le compte n'existe pas : \n Assurez-vous de remplir les conditions  de candidature");
            return "loginpfe";
        } else if (!user.isValidation()) {
            // Account not validated
            model.addAttribute("errorMessage", "Veuillez valider votre compte avant de vous connecter");
            return "loginpfe";
        } else if(!user.getPassword().equals(password)){
            // virfier si le mots de passe est vrait 
            model.addAttribute("errorMessage", "Le mot de passe est erroné");   
            return "loginpfe";
        } 
        else {
            
            //Set ID accounte  in session
            session.setAttribute("userID", user.getId());
 

            // Account validated, redirect to profile page according to role
            if (user.getRole().equals("etudiant")) {
                return "redirect:/Etudiant/profile/"+user.getId();
            } else if (user.getRole().equals("encadrant")) {
                return "redirect:/Encadrant/profile/"+user.getId();
            } else if (user.getRole().equals("admin")) {
                return "redirect:/Admin/profile/"+user.getId();
            } else {
                // Unknown role
                model.addAttribute("errorMessage", "Role inconnu");
                return "loginpfe";
            }
        }
    }
}
