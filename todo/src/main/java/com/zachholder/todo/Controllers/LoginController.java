package com.zachholder.todo.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zachholder.todo.Models.UserItem;
import com.zachholder.todo.Models.data.ItemData;
import com.zachholder.todo.comparators.AisleComparator;
import com.zachholder.todo.comparators.CompoundComparator;
import com.zachholder.todo.comparators.NameComparator;


@Controller
@RequestMapping(value = "login")
public class LoginController {
	
	@RequestMapping(value="")
    public String login() {  
    	
        return "login/login";
    }



}
