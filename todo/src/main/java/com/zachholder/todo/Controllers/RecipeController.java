package com.zachholder.todo.Controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zachholder.todo.Models.Item;
import com.zachholder.todo.Models.Recipe;
import com.zachholder.todo.Models.data.AisleDao;
import com.zachholder.todo.Models.data.GroceryItemDao;
import com.zachholder.todo.Models.data.GroceryTypeDao;
import com.zachholder.todo.Models.data.ItemData;
import com.zachholder.todo.Models.data.RecipeData;

@Controller
@RequestMapping(value = "recipe")
public class RecipeController {

	@Autowired
	private GroceryTypeDao groceryTypeDao;
	
	@Autowired
	private GroceryItemDao groceryItemDao;
	
	@Autowired
	private AisleDao aisleDao;
	
    @RequestMapping(value = "")
    public String index(Model model) {  
    	
    	model.addAttribute("recipes", RecipeData.getRecipes());
    	model.addAttribute("title", "My Recipes");
    	model.addAttribute("recipe", new Recipe());
        return "recipe/recipe";
    }
    
    @RequestMapping(value="", method = RequestMethod.POST)
    public String addRecipe(Model model, @Valid @ModelAttribute Recipe recipe, Errors errors, int[] recipeIds) {
    	
     	if (errors.hasErrors() || (recipe.getName() == null && recipeIds == null)) {
    		model.addAttribute("recipes", RecipeData.getRecipes());
        	model.addAttribute("title", "My Recipes");
        	return "recipe/recipe";
    	}
    	
    	if (recipeIds != null) {
	    	for (int recipeId : recipeIds) {
	    		Recipe currentRecipe = RecipeData.getById(recipeId);
	    			for (Item recipeItem: currentRecipe.getRecipeItems()) {
	    				ItemData.add(recipeItem);
	    			}
	    		}
	        }
    	
    	if (recipeIds == null && recipe.getName().length() > 0) {
    		RecipeData.add(recipe);
        	return "redirect:/recipe/" + recipe.getId();
    	}
    	
    	return "recipe/recipe";
    }
    
    @RequestMapping(value = "{recipeId}", method = RequestMethod.GET)
    public String displayRecipeForm(Model model, @PathVariable int recipeId){

    	Recipe recipe = RecipeData.getById(recipeId);
        model.addAttribute(recipe);
    	model.addAttribute("title", recipe.getName());
    	model.addAttribute("item", new Item());
        return "recipe/recipeitems";
    }
    
    @RequestMapping(value = "{recipeId}", method = RequestMethod.POST)
    public String addRecipeItems(Model model, @Valid @ModelAttribute Item item, Errors errors, @PathVariable int recipeId, int[] itemIds) { 
    	Item recipeItem = new Item(item.getName());
    	Recipe recipe = RecipeData.getById(recipeId);
    	
    	if (errors.hasErrors() || (item.getName() == null && itemIds == null)){
    		model.addAttribute(recipe);
        	model.addAttribute("title", recipe.getName());
        	model.addAttribute("item", item);
    		return "recipe/recipeitems";
    	}
    	
    	if (itemIds != null) {
	    	for (int itemId : itemIds) {
	    		recipe.removeItem(itemId);
	            ItemData.remove(itemId);
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
    		
	    	ItemData.add(recipeItem);
	    	recipe.addItem(recipeItem);
    	}
    	return "redirect:/recipe/" + recipeId;
    }
    	
}


