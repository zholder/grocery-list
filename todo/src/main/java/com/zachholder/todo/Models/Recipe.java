package com.zachholder.todo.Models;

import java.util.List;

import javax.validation.constraints.Size;

public class Recipe {

    private int id;
    private static int nextId = 1;

    @Size(min=2, max=20)
	private String name;
	private List<Item> recipe;

	public Recipe() {
		this.name = null;
        id = nextId;
        nextId++;
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

	public void setRecipe(List<Item> recipe) {
		this.recipe = recipe;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public List<Item> getRecipe() {
		return recipe;
	}

	
	
}
