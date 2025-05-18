package com.gestionpfes.adnan.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import jakarta.servlet.http.HttpSession;

@Controller
public class NavbarController {
 
    @GetMapping("/Admin/users/categorie")
    public String gategoriepage(){
        return"categorieSelect";
    }

    @GetMapping("/logout")
    public String logout(RedirectAttributes ra , Model model,HttpSession session){
        session.invalidate();
        ra.addFlashAttribute("messagesucces", "Déconnecté avec succès");
        return"redirect:/login";
    }

    
}
