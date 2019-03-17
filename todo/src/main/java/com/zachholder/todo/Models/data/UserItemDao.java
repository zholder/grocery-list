package com.zachholder.todo.Models.data;


import javax.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.zachholder.todo.Models.UserItem;

@Repository
@Transactional
public interface UserItemDao extends CrudRepository<UserItem, Integer>{

	
}