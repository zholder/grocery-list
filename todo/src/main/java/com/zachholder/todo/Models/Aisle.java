package com.zachholder.todo.Models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
public class Aisle {
	@Id
	@GeneratedValue
	private int id;
	
	private String name;
		
	@Column(unique=true)
	private int position;
	
    @OneToMany
    @JoinColumn(name = "aisle_id")

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getPosition() {
		return position;
	}

	public Aisle() {
	}
    
}
