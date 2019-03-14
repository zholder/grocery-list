package com.zachholder.todo.Controllers;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.zachholder.todo.Models.User;
import com.zachholder.todo.Models.data.UserDao;

@Controller
public class SignUpController {
	
	@Autowired
	private UserDao userDao;
	
	public static String encryptePassword(String password) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hashedPassword = passwordEncoder.encode(password);
		return hashedPassword;
		}
	
	@RequestMapping(value="signup")
    public String showSignUp(Model model) {  
    	model.addAttribute("user", new User());
        return "signup/signup";
    }
	
	@RequestMapping(value="signup", method = RequestMethod.POST)
	public String addUser(Model model, @Valid @ModelAttribute User user, Errors errors) {
		if (errors.hasErrors()) {
    		model.addAttribute(user);
        	return "signup/signup";
    	}
		else {
			String hashedPW = encryptePassword(user.getPassword());
			user.setPassword(hashedPW);
			userDao.save(user);
			return "redirect:/";
		}

		
		}
}