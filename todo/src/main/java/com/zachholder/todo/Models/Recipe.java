package com.zachholder.todo.Models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;

@Entity
public class Recipe {
	
	@Id
	@GeneratedValue
    private int id;

    @Size(min=2, max=40)
	private String name;
    
    @ManyToOne
    private User owner;
    
    @ManyToMany
	private List<UserItem> items;

	public Recipe() {
	}
	
	public Recipe(String name, User owner) {
		super();
		this.name = name;
		this.owner = owner;
	}

	public void addItem(UserItem item) {
		this.items.add(item);
	}
	
    public void removeItem(int id) {
        UserItem itemToRemove = getByItemId(id);
        this.items.remove(itemToRemove);
    }
	
    public  UserItem getByItemId(int id) {

        UserItem theItem = null;
        for (UserItem candidateItem : items) {
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
	
	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public List<UserItem> getRecipeItems() {
		return items;
	}

	
	
}
