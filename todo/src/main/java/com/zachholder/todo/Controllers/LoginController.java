package com.zachholder.todo.Controllers;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
@RequestMapping(value = "login")
public class LoginController {
	
	private boolean userIsLoggedIn() {
		return SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser");
	}
	
	@RequestMapping(value="")
    public String login(Model model) {  
		if (userIsLoggedIn()){
			model.addAttribute("loggedInUser", "");
		} else {
			model.addAttribute("loggedInUser", SecurityContextHolder.getContext().getAuthentication().getName());
		}
    	model.addAttribute("title", "Login to Grocery List");
        return "login/login";
    }



}
