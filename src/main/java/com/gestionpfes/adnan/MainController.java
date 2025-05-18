package com.gestionpfes.adnan;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    // @GetMapping("/login")
	// public String login() {
	// 	return "login";
	// }
	// @GetMapping("/profile")
	// public String profile() {
	// 	return "profile";
	// }
	
	@GetMapping("/")
	public String home() {
		return "loginpfe";
	}
	
}

