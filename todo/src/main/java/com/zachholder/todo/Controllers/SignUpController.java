package com.zachholder.todo.Controllers;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
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
//	
//    @Autowired
//    protected AuthenticationManager authenticationManager;

    
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
	public String addUser(Model model, @ModelAttribute @Valid User user, Errors errors, HttpServletRequest request) {
		if (errors.hasErrors()) {
			model.addAttribute(user);
        	return "signup/signup";
    	}
		
		else if (userDao.findByEmail(user.getEmail()).size() > 0) {
			model.addAttribute("error", "A user with this email already exists!");
        	return "signup/signup";
		}

		else {
			String loginPW = user.getPassword();
			String hashedPW = encryptePassword(user.getPassword());
			String email = user.getEmail();
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