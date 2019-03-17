package com.zachholder.todo.Models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;

@Entity
public class GroceryItem {

	@Id
	@GeneratedValue
    private int id;
	
    @Size(min=2, max=20)
    private String name;
    
    @ManyToOne
    private ItemType itemType;

    @ManyToOne
    private Aisle aisle;
    
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public ItemType getItemType() {
		return itemType;
	}
	
	public Aisle getAisle() {
		return aisle;
	}

	public void setItemType(ItemType itemType) {
		this.itemType = itemType;
	}

	public void setAisle(Aisle aisle) {
		this.aisle = aisle;
	}
	
	public GroceryItem() {
		
	}
	
	public GroceryItem(int id, String name, ItemType itemType) {
		this.id = id;
		this.name = name;
		this.itemType = itemType;
	}
    
}
