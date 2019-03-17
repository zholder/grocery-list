package com.zachholder.todo.Models.data;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.zachholder.todo.Models.Recipe;

@Repository
@Transactional
public interface RecipeDao  extends CrudRepository <Recipe, Integer>{

}
