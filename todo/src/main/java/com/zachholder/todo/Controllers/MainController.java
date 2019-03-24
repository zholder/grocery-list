package com.zachholder.todo.Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.zachholder.todo.Models.UserItem;
import com.zachholder.todo.Models.User;
import com.zachholder.todo.Models.data.AisleDao;
import com.zachholder.todo.Models.data.GroceryItemDao;
import com.zachholder.todo.Models.data.GroceryTypeDao;
import com.zachholder.todo.Models.data.UserDao;
import com.zachholder.todo.Models.data.UserItemDao;

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
	
	private String cleanUpSearch(String searchTerm) {
		if (searchTerm != null && searchTerm.length() > 0 && searchTerm.toLowerCase().charAt(searchTerm.length() - 1) == 's') {
			searchTerm = searchTerm.substring(0, searchTerm.length() - 1);
	    }
		return searchTerm;
	}
	
    @RequestMapping(value = "")
    public String index(Model model) {  
    	User currentUser = findCurrentUser();
    	model.addAttribute("items", userItemDao.findAllByOwnerAndOnListOrderByAisleAscNameAsc(currentUser, true));
    	model.addAttribute("title", currentUser.getFirstName() + "'s Grocery List");
    	model.addAttribute("userItem", new UserItem());

    	return "item/index";
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public String addItems(Model model, @ModelAttribute @Valid UserItem item, Errors errors, int[] itemIds) { 
    	User currentUser = findCurrentUser();

    	if (errors.hasErrors() || (item.getName() == null && itemIds == null)){
        	model.addAttribute("items", userItemDao.findAllByOwnerAndOnListOrderByAisleAscNameAsc(currentUser, true));
        	model.addAttribute("title", currentUser.getFirstName() + "'s Grocery List");
    		return "item/index";
    	}
    	
    	
    	if (itemIds != null) {
	    	for (int itemId : itemIds) {
	    		UserItem currentUserItem = userItemDao.findById(itemId).get();
	    		if (currentUserItem.isOnRecipe()) {
	    			currentUserItem.setOnList(false);
	    			userItemDao.save(currentUserItem);
	    		} else {
		    		userItemDao.deleteById(itemId);
	    		}
	        }
    	}
    	
    	if (itemIds == null && item.getName().length() > 0) {
    		item.setOwner(currentUser);
    		item.setOnList(true);
    		if (groceryItemDao.findByName(item.getName()) == null ) {
    			item.setType(groceryTypeDao.findById(1).get());
    			item.setAisle(aisleDao.findById(1).get());
            	userItemDao.save(item);
    		}
    		else {
        	item.setType(groceryItemDao.findByName(item.getName().toLowerCase()).getItemType());
        	item.setAisle(groceryItemDao.findByName(item.getName().toLowerCase()).getAisle());
        	userItemDao.save(item);
    		}
    	}
    	
    	if (itemIds == null && item.getName().length() > 0) {
    		item.setOwner(currentUser);
    		item.setOnList(true);
        	String searchTerm = cleanUpSearch(item.getName()).toLowerCase();

    		if (groceryItemDao.findByName(searchTerm) == null ) {
    		item.setType(groceryTypeDao.findById(1).get());
    		item.setAisle(aisleDao.findById(1).get());
    		}
    	
    		else {
    		item.setType(groceryItemDao.findByName(searchTerm).getItemType());
    		item.setAisle(groceryItemDao.findByName(searchTerm).getAisle());
    		}
    		userItemDao.save(item);
    	}
    	
    	return "redirect:"; 	
    }
    
    @RequestMapping(value = "", method = RequestMethod.POST, params="itemId")
    public String deleteItem(Model model, @RequestParam int itemId){
    	UserItem currentUserItem = userItemDao.findById(itemId).get();
		if (currentUserItem.isOnRecipe()) {
			currentUserItem.setOnList(false);
			userItemDao.save(currentUserItem);
		} else {
    		userItemDao.deleteById(itemId);
		}
    	return "redirect:/";
    }
    
    @RequestMapping(value = "", method = RequestMethod.POST, params="unCheckItemId")
    public String uncheckItem(Model model, @RequestParam int unCheckItemId){
    	UserItem currentUserItem = userItemDao.findById(unCheckItemId).get();
    	currentUserItem.setChecked(true);
    	userItemDao.save(currentUserItem);
    	return "redirect:/";
    }
    
    @RequestMapping(value = "", method = RequestMethod.POST, params="checkItemId")
    public String checkItem(Model model, @RequestParam int checkItemId){
    	UserItem currentUserItem = userItemDao.findById(checkItemId).get();
    	currentUserItem.setChecked(false);
    	userItemDao.save(currentUserItem);
    	return "redirect:/";
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
