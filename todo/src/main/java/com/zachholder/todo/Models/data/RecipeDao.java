package com.zachholder.todo.Models.data;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.zachholder.todo.Models.Recipe;
import com.zachholder.todo.Models.User;

@Repository
@Transactional
public interface RecipeDao  extends CrudRepository <Recipe, Integer>{
	
	public List<Recipe> findAllByOwner(User owner);
	
}
