package com.zachholder.todo.Models;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Size;

public class Recipe {

    private int id;
    private static int nextId = 1;

    @Size(min=2, max=40)
	private String name;
	private ArrayList<UserItem> recipeItems = new ArrayList<UserItem> ();

	public Recipe() {
		this.name = null;
        id = nextId;
        nextId++;
	}
	
	public Recipe(String name) {
		super();
		this.name = name;
	}

	public void addItem(UserItem item) {
		this.recipeItems.add(item);
	}
	
    public void removeItem(int id) {
        UserItem itemToRemove = getByItemId(id);
        this.recipeItems.remove(itemToRemove);
    }
	
    public  UserItem getByItemId(int id) {

        UserItem theItem = null;
        for (UserItem candidateItem : recipeItems) {
            if (candidateItem.getId() == id) {
            	theItem = candidateItem;
            }
        }
        return theItem;
    }
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public List<UserItem> getRecipeItems() {
		return recipeItems;
	}

	
	
}
