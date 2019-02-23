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
	
}
