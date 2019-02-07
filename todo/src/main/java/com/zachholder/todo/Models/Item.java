package com.zachholder.todo.Models;

import javax.validation.constraints.Size;

public class Item {
	
    private int id;
    private static int nextId = 1;

    @Size(min=2, max=20)
	private String name;

    private ItemType type;
    
    private Aisle aisle;
    
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

	public Item() {
		this.name = null;
        id = nextId;
        nextId++;
	}
	
	public Item(String name) {
		this();
		this.name = name; 
	}
	
}
