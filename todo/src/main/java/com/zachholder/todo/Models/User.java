package com.zachholder.todo.Models;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Entity
public class User {
	

	@Id
	@GeneratedValue
    private int id;
	    
	@NotNull
    @Size(min=1, max=30)
	private String firstName;

	@NotNull
    @Size(min=1, max=50)
	private String lastName;
    
	@NotNull
    @Size(min=1, max=30)
	private String email;
  
	private String password;

    private boolean enabled;

    @OneToMany
    @JoinColumn(name = "owner_id")
    private List<UserItem> userItems = new ArrayList<>();
    
    @OneToMany
    @JoinColumn(name = "owner_id")
    private List<Recipe> recipes = new ArrayList<>();
    
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
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
    
	
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public User() {
        this.enabled = true;
	}
	
	public User(String firstName, String lastName, String email, String password) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
	}

}
