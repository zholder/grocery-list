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

    @RequestMapping(value = "")
    public String index(Model model) {  
    	User currentUser = findCurrentUser();

    	model.addAttribute("recipes", recipeDao.findAllByOwner(currentUser));
    	model.addAttribute("title", currentUser.getFirstName() + "'s Recipes");
    	model.addAttribute("recipe", new Recipe());
        return "recipe/recipe";
    }
    
    @RequestMapping(value="", method = RequestMethod.POST)
    public String addRecipe(Model model, @Valid @ModelAttribute Recipe recipe, Errors errors, int[] recipeIds) {
    	User currentUser = findCurrentUser();

    	
    	if (errors.hasErrors() || (recipe.getName() == null && recipeIds == null)) {
        	model.addAttribute("recipes", recipeDao.findAllByOwner(currentUser));
        	model.addAttribute("title", currentUser.getFirstName() + "'s Recipes");
        	return "recipe/recipe";
    	}
    	
    	if (recipeIds != null) {
    		
	    	for (int recipeId : recipeIds) {
	    		Recipe currentRecipe = recipeDao.findById(recipeId).get();
	    			for (UserItem recipeItem: currentRecipe.getRecipeItems()) {
	    				recipeItem.setOnList(true);
	    				userItemDao.save(recipeItem);
	    			}
	    		}
	    		return "redirect:/";
	        }
    	
    	if (recipeIds == null && recipe.getName().length() > 0) {
    		recipe.setOwner(currentUser);
    		recipeDao.save(recipe);
        	return "redirect:/recipe/" + recipe.getId();
    	}
    	
    	return "recipe/recipe";
    }
    
    @RequestMapping(value = "", method = RequestMethod.POST, params="recipeId")
    public String deleteRecipe(Model model, @RequestParam int recipeId){
    	recipeDao.deleteById(recipeId);
    	return "redirect:/recipe";
    }

    
    @RequestMapping(value = "{recipeId}", method = RequestMethod.GET)
    public String displayRecipeForm(Model model, @PathVariable int recipeId){

    	Recipe recipe = recipeDao.findById(recipeId).get();
        model.addAttribute(recipe);
    	model.addAttribute("title", recipe.getName());
    	model.addAttribute("item", new UserItem());
        return "recipe/recipeitems";
    }
    
    @RequestMapping(value = "{recipeId}", method = RequestMethod.POST)
    public String addRecipeItems(Model model, @Valid @ModelAttribute UserItem item, Errors errors, @PathVariable int recipeId, int[] itemIds) { 
    	User currentUser = findCurrentUser();

    	UserItem recipeItem = new UserItem(item.getName(), currentUser);
    	Recipe recipe = recipeDao.findById(recipeId).get();
    	
    	if (errors.hasErrors() || (item.getName() == null && itemIds == null)){
    		model.addAttribute(recipe);
        	model.addAttribute("title", recipe.getName());
        	model.addAttribute("item", item);
    		return "recipe/recipeitems";
    	}
    	
    	if (itemIds != null) {
	    	for (int itemId : itemIds) {
	    		UserItem theItem = userItemDao.findById(itemId).get();
	    		if (! theItem.isOnList()) {
	    			userItemDao.deleteById(itemId);
	    		} else {
		    		recipe.removeItem(itemId);
		    		theItem.setOnRecipe(false);
		    		// keeps on the user list
	    		}
		    	recipeDao.save(recipe);
	        }
    	}
    	
    	if (itemIds == null && recipeItem.getName().length() > 0) {
    		
    		if (groceryItemDao.findByName(recipeItem.getName()) == null ) {
    		recipeItem.setType(groceryTypeDao.findById(1).get());
    		recipeItem.setAisle(aisleDao.findById(1).get());
    		}
    	
    		else {
    		recipeItem.setType(groceryItemDao.findByName(recipeItem.getName().toLowerCase()).getItemType());
    		recipeItem.setAisle(groceryItemDao.findByName(recipeItem.getName().toLowerCase()).getAisle());
    		}
    		recipeItem.setOnRecipe(true);
    		recipeItem.setOnList(false);
    		userItemDao.save(recipeItem);
	    	recipe.addItem(recipeItem);
	    	recipeDao.save(recipe);
    	}
    	
    	
    	return "redirect:/recipe/" + recipeId;
    }
    	
}


