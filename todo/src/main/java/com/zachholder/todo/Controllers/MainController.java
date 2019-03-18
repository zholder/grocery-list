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
    	model.addAttribute("item", new UserItem());

    	return "item/index";
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public String addItems(Model model, @ModelAttribute @Valid UserItem userItem, Errors errors, int[] itemIds) { 
    	User currentUser = findCurrentUser();

    	if (errors.hasErrors() || (userItem.getName() == null && itemIds == null)){
    		model.addAttribute("items", userItemDao.findAllByOwner(currentUser));
        	model.addAttribute("title", currentUser.getFirstName() + "'s Grocery List");
        	model.addAttribute("itemTypes", groceryTypeDao.findAll());
    		return "item/index";
    	}
    	
    	if (itemIds != null) {
	    	for (int itemId : itemIds) {
	    		userItemDao.deleteById(itemId);
	        }
    	}
    	
    	if (itemIds == null && userItem.getName().length() > 0) {
    		
    		if (groceryItemDao.findByName(userItem.getName()) == null ) {
    			userItem.setType(groceryTypeDao.findById(1).get());
    			userItem.setAisle(aisleDao.findById(1).get());
            	userItem.setOwner(currentUser);
            	userItemDao.save(userItem);
    		}
    		else {
        	userItem.setType(groceryItemDao.findByName(userItem.getName().toLowerCase()).getItemType());
        	userItem.setAisle(groceryItemDao.findByName(userItem.getName().toLowerCase()).getAisle());
        	userItem.setOwner(currentUser);
        	userItemDao.save(userItem);
    		}
    	}
    	return "redirect:";
    }
    
    @RequestMapping(value = "edit/{itemId}", method = RequestMethod.GET)
    public String displayEditForm(Model model, @PathVariable int itemId){

        UserItem item = userItemDao.findById(itemId).get();
    	model.addAttribute("title", "Edit " + item.getName());
        model.addAttribute("item", item);
        model.addAttribute("itemTypes", groceryTypeDao.findAll());
        model.addAttribute("aisles", aisleDao.findAll());
        return  "item/item";
    }

    @RequestMapping(value = "edit/{itemId}", method = RequestMethod.POST)
    public String processEditForm(@ModelAttribute @Valid UserItem item, Errors errors, @PathVariable int itemId, Model model){

    	if (errors.hasErrors()){
        	model.addAttribute("title", "Edit " + item.getName());
    		model.addAttribute(item);
            model.addAttribute("itemTypes", groceryTypeDao.findAll());
            model.addAttribute("aisles", aisleDao.findAll());
            return "item/item";
       }
    	
    	UserItem editedItem = userItemDao.findById(itemId).get();
        editedItem.setName(item.getName());
        editedItem.setType(item.getType());
    	editedItem.setAisle(item.getAisle());
    	userItemDao.save(editedItem);
        return "redirect:/";
    }

}
