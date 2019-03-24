package com.zachholder.todo.Controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.zachholder.todo.Models.UserItem;
import com.zachholder.todo.Models.Recipe;
import com.zachholder.todo.Models.User;
import com.zachholder.todo.Models.data.AisleDao;
import com.zachholder.todo.Models.data.GroceryItemDao;
import com.zachholder.todo.Models.data.GroceryTypeDao;
import com.zachholder.todo.Models.data.RecipeDao;
import com.zachholder.todo.Models.data.UserDao;
import com.zachholder.todo.Models.data.UserItemDao;

@Controller
@RequestMapping(value = "recipe")
public class RecipeController {

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
	
	@Autowired
	private RecipeDao recipeDao;
	
	private User findCurrentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	User currentUser = userDao.findByEmail(auth.getName()).get(0);
    	return currentUser;
	}
	
	private boolean userIsLoggedIn() {
		return SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser");
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
    	
		if (userIsLoggedIn()){
			model.addAttribute("loggedInUser", "");
		} else {
			model.addAttribute("loggedInUser", SecurityContextHolder.getContext().getAuthentication().getName());
		}
		
    	model.addAttribute("recipes", recipeDao.findAllByOwner(currentUser));
    	model.addAttribute("title", currentUser.getFirstName() + "'s Recipes");
    	model.addAttribute("recipe", new Recipe());
        return "recipe/recipe";
    }
    
    @RequestMapping(value="", method = RequestMethod.POST)
    public String addRecipe(Model model, @Valid @ModelAttribute Recipe recipe, Errors errors) {
    	User currentUser = findCurrentUser();
    	
    	if (errors.hasErrors() || recipe.getName() == null) {
    		if (userIsLoggedIn()){
    			model.addAttribute("loggedInUser", "");
    		} else {
    			model.addAttribute("loggedInUser", SecurityContextHolder.getContext().getAuthentication().getName());
    		}
        	model.addAttribute("recipes", recipeDao.findAllByOwner(currentUser));
        	model.addAttribute("title", currentUser.getFirstName() + "'s Recipes");
        	return "recipe/recipe";
    	}
	
    	else {
    		recipe.setOwner(currentUser);
    		recipeDao.save(recipe);
        	return "redirect:/recipe/" + recipe.getId();
    	}
    }
    
    @RequestMapping(value = "", method = RequestMethod.POST, params="recipeId")
    public String deleteRecipe(Model model, @RequestParam int recipeId){
    	recipeDao.deleteById(recipeId);
    	return "redirect:/recipe";
    }

    @RequestMapping(value = "", method = RequestMethod.POST, params="recipeToList")
    public String addRecipeToList(Model model, @RequestParam int recipeToList){
		Recipe currentRecipe = recipeDao.findById(recipeToList).get();
		for (UserItem recipeItem: currentRecipe.getRecipeItems()) {
			recipeItem.setOnList(true);
			userItemDao.save(recipeItem);
		}
    	return "redirect:/recipe";
    }   
    
    @RequestMapping(value = "{recipeId}", method = RequestMethod.GET)
    public String displayRecipeForm(Model model, @PathVariable int recipeId){
    	Recipe recipe = recipeDao.findById(recipeId).get();
    	
		if (userIsLoggedIn()){
			model.addAttribute("loggedInUser", "");
		} else {
			model.addAttribute("loggedInUser", SecurityContextHolder.getContext().getAuthentication().getName());
		}
        model.addAttribute(recipe);
    	model.addAttribute("title", recipe.getName());
    	model.addAttribute("userItem", new UserItem());
        return "recipe/recipeitems";
    }
    
    @RequestMapping(value = "{recipeId}", method = RequestMethod.POST)
    public String addRecipeItems(Model model, @Valid @ModelAttribute UserItem item, Errors errors, @PathVariable int recipeId, int[] itemIds) { 
    	User currentUser = findCurrentUser();

    	UserItem recipeItem = new UserItem(item.getName(), currentUser);
    	Recipe recipe = recipeDao.findById(recipeId).get();
    	
    	if (errors.hasErrors() || (item.getName() == null && itemIds == null)){
    		if (userIsLoggedIn()){
    			model.addAttribute("loggedInUser", "");
    		} else {
    			model.addAttribute("loggedInUser", SecurityContextHolder.getContext().getAuthentication().getName());
    		}
    		model.addAttribute(recipe);
        	model.addAttribute("title", recipe.getName());
    		return "recipe/recipeitems";
    	}
    	
    	// adds and categorizes items but does not set on list
    	if (itemIds == null && recipeItem.getName().length() > 0) {
    		
        	String searchTerm = cleanUpSearch(recipeItem.getName()).toLowerCase();

    		if (groceryItemDao.findByName(searchTerm) == null ) {
    		recipeItem.setType(groceryTypeDao.findById(1).get());
    		recipeItem.setAisle(aisleDao.findById(1).get());
    		}
    	
    		else {
    		recipeItem.setType(groceryItemDao.findByName(searchTerm).getItemType());
    		recipeItem.setAisle(groceryItemDao.findByName(searchTerm).getAisle());
    		}
    		recipeItem.setOnRecipe(true);
    		recipeItem.setOnList(false);
    		userItemDao.save(recipeItem);
	    	recipe.addItem(recipeItem);
	    	recipeDao.save(recipe);
    	}
    	
    	
    	return "redirect:/recipe/" + recipeId;
    }
    
    @RequestMapping(value = "{recipeId}", method = RequestMethod.POST, params="itemId")
    public String deleteItem(Model model, @PathVariable int recipeId, @RequestParam int itemId){
    	Recipe recipe = recipeDao.findById(recipeId).get();
		UserItem theItem = userItemDao.findById(itemId).get();
		
		if (! theItem.isOnList()) {
			userItemDao.deleteById(itemId);
		} else {
    		recipe.removeItem(itemId);
    		theItem.setOnRecipe(false);
    		// keeps on the user list
		}
    	recipeDao.save(recipe);
    	return "redirect:/recipe/" + recipeId;
    }
    
    	
}


