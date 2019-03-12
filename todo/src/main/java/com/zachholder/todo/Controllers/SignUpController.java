package com.zachholder.todo.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping(value = "signup")
public class SignUpController {
	
	@RequestMapping(value="")
    public String signup() {  
    	
        return "signup/signup";
    }

}



