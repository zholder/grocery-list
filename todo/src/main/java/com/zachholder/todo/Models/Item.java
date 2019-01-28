package com.zachholder.todo.Models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Item {
    private int id;
    private static int nextId = 1;

    @Size(min=2, max=20)
	private String name;

	public String getName() {
		return name;
	}
	
	public int getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Item() {
        id = nextId;
        nextId++;
	}
	
	public Item(String name) {
		this();
		this.name = name; 
	}
	
}
