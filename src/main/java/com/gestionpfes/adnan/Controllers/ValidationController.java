package com.gestionpfes.adnan.Controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gestionpfes.adnan.models.User;
import com.gestionpfes.adnan.services.UserService;
import org.springframework.ui.Model;




@Controller
public class ValidationController {

    @Autowired
    private UserService userService;

    @GetMapping("/validate")
    public String validationForm() {
        return "valider";
    }

    @PostMapping("/validation/check")
    public String validationCheck(@RequestParam("email") String email, @RequestParam("name") String name, 
        @RequestParam("lastname") String lastname, @RequestParam("password") String password,
        @RequestParam("passwordverify") String passwordverify, Model model, RedirectAttributes red)  {

        // Check if user with given email exists
        User user = userService.getUserByEmail(email); 

        if (user == null) {
            // Account doesn't exist
            model.addAttribute("errorMessage", "Veuillez vous assurer que vous remplissez les conditions pour PFE");
            return "valider";
        } else if (user.isValidation()) {
            // Account already validated
            model.addAttribute("errorMessage", "Ce compte est déjà validé");
            return "valider";
        } else if (!user.getName().equals(name) || !user.getLastname().equals(lastname)) {
            // Name or lastname is wrong
            model.addAttribute("errorMessage", "Le nom ou le prénom est incorrect");
            return "valider";
        } else if (!password.equals(passwordverify)) {
            // Account already validated
            model.addAttribute("errorMessage", "le mot de passe ne correspond pas");
            return "valider";
        } else {
            // Update user password and set validation flag
            user.setPassword(password);
            user.setValidation(true);
            userService.saveUser(user);
            red.addFlashAttribute("messagesucces", "le compte a été validé avec succès");
            return "redirect:/login?success";
        }
    }
}
