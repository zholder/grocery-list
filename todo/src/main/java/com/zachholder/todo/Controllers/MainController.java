package com.zachholder.todo.Controllers;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zachholder.todo.Models.UserItem;
import com.zachholder.todo.Models.User;
import com.zachholder.todo.Models.data.AisleDao;
import com.zachholder.todo.Models.data.GroceryItemDao;
import com.zachholder.todo.Models.data.GroceryTypeDao;
import com.zachholder.todo.Models.data.UserDao;
import com.zachholder.todo.Models.data.UserItemDao;
import com.zachholder.todo.comparators.AisleComparator;
import com.zachholder.todo.comparators.CompoundComparator;
import com.zachholder.todo.comparators.NameComparator;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;

@Controller
public class MainController {
	
	@Autowired
	private GroceryTypeDao groceryTypeDao;
	
	@Autowired
	private GroceryItemDao groceryItemDao;

	@Autowired
	private AisleDao aisleDao;
	
	@Autowired 
	private UserDao userDao;
	
	@Autowired 
	private UserItemDao userItemDao;
	
	private User findCurrentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	User currentUser = userDao.findByEmail(auth.getName()).get(0);
    	return currentUser;
	}
	
    @RequestMapping(value = "")
    public String index(Model model) {  
    	User currentUser = findCurrentUser();
        CompoundComparator comparator = new CompoundComparator();
        comparator.add(new AisleComparator());
        comparator.add(new NameComparator());
    	model.addAttribute("items", userItemDao.findAllByOwner(currentUser));
    	model.addAttribute("title", currentUser.getFirstName() + "'s Grocery List");
    	model.addAttribute("userItem", new UserItem());

    	return "item/index";
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public String addItems(Model model, @ModelAttribute @Valid UserItem item, Errors errors, int[] itemIds) { 
    	User currentUser = findCurrentUser();

    	if (errors.hasErrors() || (item.getName() == null && itemIds == null)){
    		System.out.println("error");
    		model.addAttribute("items", userItemDao.findAllByOwner(currentUser));
        	model.addAttribute("title", currentUser.getFirstName() + "'s Grocery List");
    		return "item/index";
    	}
    	
    	
    	if (itemIds != null) {
	    	for (int itemId : itemIds) {
	    		userItemDao.deleteById(itemId);
	        }
    	}
    	
    	if (itemIds == null && item.getName().length() > 0) {
    		
    		if (groceryItemDao.findByName(item.getName()) == null ) {
    			item.setType(groceryTypeDao.findById(1).get());
    			item.setAisle(aisleDao.findById(1).get());
            	item.setOwner(currentUser);
            	userItemDao.save(item);
    		}
    		else {
        	item.setType(groceryItemDao.findByName(item.getName().toLowerCase()).getItemType());
        	item.setAisle(groceryItemDao.findByName(item.getName().toLowerCase()).getAisle());
        	item.setOwner(currentUser);
        	userItemDao.save(item);
    		}
    	}
    	return "redirect:";
    }
    
    @RequestMapping(value = "edit/{itemId}", method = RequestMethod.GET)
    public String displayEditForm(Model model, @PathVariable int itemId){

        UserItem userItem = userItemDao.findById(itemId).get();
    	model.addAttribute("title", "Edit " + userItem.getName());
        model.addAttribute(userItem);
        model.addAttribute("itemTypes", groceryTypeDao.findAll());
        model.addAttribute("aisles", aisleDao.findAll());
        return  "item/item";
    }

    @RequestMapping(value = "edit/{itemId}", method = RequestMethod.POST)
    public String processEditForm(@ModelAttribute @Valid UserItem userItem, Errors errors, @PathVariable int itemId, Model model){

    	if (errors.hasErrors()){
        	model.addAttribute("title", "Edit " + userItem.getName());
    		model.addAttribute(userItem);
            model.addAttribute("itemTypes", groceryTypeDao.findAll());
            model.addAttribute("aisles", aisleDao.findAll());
            return "item/item";
       }
    	
    	UserItem editedItem = userItemDao.findById(itemId).get();
        editedItem.setName(userItem.getName());
        editedItem.setType(userItem.getType());
    	editedItem.setAisle(userItem.getAisle());
    	userItemDao.save(editedItem);
        return "redirect:/";
    }

}
