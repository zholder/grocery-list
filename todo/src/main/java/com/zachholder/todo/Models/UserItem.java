package com.zachholder.todo.Models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;


@Entity
public class UserItem {
	
	@Id
	@GeneratedValue
    private int id;

    @Size(min=2, max=20)
	private String name;

    @ManyToOne
    private ItemType type;
    
    @ManyToOne
    private Aisle aisle;
    
    @ManyToOne
    private User owner;
    
    @ManyToMany(mappedBy = "items")
    private List<Recipe> recipes;

    private boolean onList;
    
    private boolean onRecipe;
    
	public String getName() {
		return name;
	}
	
	public int getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ItemType getType() {
		return type;
	}

	public void setType(ItemType type) {
		this.type = type;
	}

	public Aisle getAisle() {
		return aisle;
	}

	public void setAisle(Aisle aisle) {
		this.aisle = aisle;
	}
	
	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public boolean isOnList() {
		return onList;
	}

	public void setOnList(boolean onList) {
		this.onList = onList;
	}

	public boolean isOnRecipe() {
		return onRecipe;
	}

	public void setOnRecipe(boolean onRecipe) {
		this.onRecipe = onRecipe;
	}

	public UserItem() {
	}
	
	public UserItem(String name, User owner) {
		this();
		this.name = name; 
		this.owner = owner;
		this.onList = true;
		this.onRecipe = false;
	}
	
}
