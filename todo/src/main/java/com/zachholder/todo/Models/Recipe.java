package com.zachholder.todo.Models;

import java.util.List;

import javax.validation.constraints.Size;

public class Recipe {

    private int id;
    private static int nextId = 1;

    @Size(min=2, max=20)
	private String name;
	private List<Item> recipeItems;

	public Recipe() {
		this.name = null;
        id = nextId;
        nextId++;
	}
	
	public Recipe(String name, List<Item> recipeItems) {
		super();
		this.name = name;
		this.recipeItems = recipeItems;
	}

	public void addItem(Item item) {
		recipeItems.add(item);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setRecipe(List<Item> recipeItems) {
		this.recipeItems = recipeItems;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public List<Item> getRecipe() {
		return recipeItems;
	}

	
	
}
