package com.zachholder.todo.Models;

import java.util.List;

public class Recipe {

	private String name;
	private List<Item> recipe;

	public Recipe() {
	}
	
	public Recipe(String name, List<Item> recipe) {
		super();
		this.name = name;
		this.recipe = recipe;
	}

	public void addItem(Item item) {
		recipe.add(item);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Item> getRecipe() {
		return recipe;
	}

	public void setRecipe(List<Item> recipe) {
		this.recipe = recipe;
	}

	
	
}
