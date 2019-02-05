package com.zachholder.todo.Controllers;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.zachholder.todo.Models.Item;
import com.zachholder.todo.Models.ItemType;
import com.zachholder.todo.Models.data.GroceryItemDao;
import com.zachholder.todo.Models.data.GroceryTypeDao;
import com.zachholder.todo.Models.data.ItemData;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;

@Controller
public class MainController {
	
	@Autowired
	private GroceryTypeDao groceryTypeDao;
	private GroceryItemDao groceryItemDao;

	
    @RequestMapping(value = "")
    public String index(Model model) {    
    	model.addAttribute("items", ItemData.getItems());
    	model.addAttribute("title", "To Do List");
    	model.addAttribute("item", new Item());
    	model.addAttribute("itemTypes", groceryTypeDao.findAll());
        return "index";
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public String addItems(Model model, @ModelAttribute @Valid Item item, Errors errors, int[] itemIds) { 
    	
    	if (errors.hasErrors() || (item.getName() == null && itemIds == null)){
    		model.addAttribute("items", ItemData.getItems());
        	model.addAttribute("title", "To Do List");
        	model.addAttribute("itemTypes", groceryTypeDao.findAll());
    		return "index";
    	}
    	
    	if (itemIds != null) {
	    	for (int itemId : itemIds) {
	            ItemData.remove(itemId);
	        }
    	}
    	
    	if (itemIds == null && item.getName().length() > 0) {
        	
        	ItemData.add(item);

    	}
    	
    	
    	return "redirect:";
    }

   
}
