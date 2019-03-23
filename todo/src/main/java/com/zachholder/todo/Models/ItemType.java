package com.zachholder.todo.Models;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
public class ItemType {
	
	@Id
	@GeneratedValue
	private int id;
	
	private String name;
	
    @OneToMany
    @JoinColumn(name = "item_type_id")
    private List<GroceryItem> groceryItems = new ArrayList<>();
    
    
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public ItemType() {
	}
	
	public ItemType(int id, String name) {
		this.id = id;
		this.name = name;
	}
}
