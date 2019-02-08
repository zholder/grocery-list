package com.zachholder.todo.Models.data;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.zachholder.todo.Models.GroceryItem;

@Repository
@Transactional
public interface GroceryItemDao extends CrudRepository<GroceryItem, Integer>{

    public GroceryItem findByName(String name);

}
