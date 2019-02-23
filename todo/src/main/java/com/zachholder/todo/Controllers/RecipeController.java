package com.zachholder.todo.Controllers;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zachholder.todo.Models.Recipe;
import com.zachholder.todo.Models.data.RecipeData;

@Controller
@RequestMapping(value = "recipe")
public class RecipeController {

    @RequestMapping(value = "")
    public String index(Model model) {  
    	
    	model.addAttribute("recipes", RecipeData.getRecipes());
    	model.addAttribute("title", "Recipes");
    	model.addAttribute("recipe", new Recipe());
        return "recipe/recipe";
    }
    
    @RequestMapping(value="", method = RequestMethod.POST)
    public String addRecipe(Model model, @Valid @ModelAttribute Recipe recipe, Errors errors) {
    	
    	if (errors.hasErrors()) {
    		model.addAttribute("recipes", RecipeData.getRecipes());
        	model.addAttribute("title", "Recipes");
        	return "recipe/recipe";
    	}
    	
    	RecipeData.add(recipe);
    	return "redirect:/recipe";
    }
    
  
}


