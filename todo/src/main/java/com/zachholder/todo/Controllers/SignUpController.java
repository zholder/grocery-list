package com.zachholder.todo.Controllers;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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

	private boolean userIsLoggedIn() {
		return SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser");
	}
    
	public static String encryptePassword(String password) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hashedPassword = passwordEncoder.encode(password);
		return hashedPassword;
		}
	
	@RequestMapping(value="signup")
    public String showSignUp(Model model) {  
		
		if (userIsLoggedIn()){
			model.addAttribute("loggedInUser", "");
		} else {
			model.addAttribute("loggedInUser", SecurityContextHolder.getContext().getAuthentication().getName());
		}
		
    	model.addAttribute("title", "Sign Up for Grocery List");
    	model.addAttribute("user", new User());
        return "signup/signup";
    }
	
	@RequestMapping(value="signup", method = RequestMethod.POST)
	public String addUser(Model model, @ModelAttribute @Valid User user, Errors errors, HttpServletRequest request, String verifyPassword) {
		String loginPW = user.getPassword();
		String email = user.getEmail();
		Boolean checkedErrors = false;
		
		if (userDao.findByEmail(user.getEmail()).size() > 0) {
			model.addAttribute("userError", "A user with this email already exists!");
			checkedErrors = true;
		}

		if (! loginPW.equals(verifyPassword)) {
			model.addAttribute("verifyError", "Passwords must match!");
			checkedErrors = true;
		}
		
		if (loginPW == null || loginPW.length() < 6 || loginPW.length() > 20 ) {
			model.addAttribute("passwordError", "Password must be between 6 and 20 characters!");
			checkedErrors = true;
		}
		
		if (errors.hasErrors()) {
			checkedErrors = true;
		}
		
		if (checkedErrors) {
			if (userIsLoggedIn()){
				model.addAttribute("loggedInUser", "");
			} else {
				model.addAttribute("loggedInUser", SecurityContextHolder.getContext().getAuthentication().getName());
			}
	    	model.addAttribute("title", "Sign Up for Grocery List");
	    	return "signup/signup";
		}
		
		else {
			String hashedPW = encryptePassword(user.getPassword());
			user.setPassword(hashedPW);
			userDao.save(user);
			try {
				request.login(email,loginPW);
				} catch(ServletException e) {
				}
			return "redirect:/";
			}
		
	}
}