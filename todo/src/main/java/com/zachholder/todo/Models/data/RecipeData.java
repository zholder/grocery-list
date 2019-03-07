package com.zachholder.todo.Models.data;

import java.util.ArrayList;

import com.zachholder.todo.Models.Recipe;

public class RecipeData {

	static ArrayList<Recipe> recipes = new ArrayList<>();
	
	public static ArrayList<Recipe> getRecipes() {
		return recipes;
	}

	public static void add(Recipe recipe) {
		recipes.add(recipe);
	}
	
	public static void remove(Recipe recipe) {
		recipes.remove(recipe);
	}
	
    public static Recipe getById(int id) {

        Recipe theRecipe = null;

        for (Recipe candidateRecipe : recipes) {
            if (candidateRecipe.getId() == id) {
            	theRecipe = candidateRecipe;
            }
        }

        return theRecipe;
    }
	
}
