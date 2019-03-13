package com.zachholder.todo.Models;

import javax.validation.constraints.Size;

public class User {
	
    private int id;
    private static int nextId = 1;
    
    @Size(min=1, max=30)
	private String firstName;

    @Size(min=1, max=50)
	private String lastName;
    
    @Size(min=1, max=30)
	private String email;
    
    @Size(min=6, max=20)
	private String password;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}
    
	public User() {
        id = nextId;
        nextId++;
	}
	
	public User(String firstName, String lastName, String email, String password) {
        id = nextId;
        nextId++;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
	}

}
