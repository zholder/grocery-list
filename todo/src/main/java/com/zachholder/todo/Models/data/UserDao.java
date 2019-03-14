package com.zachholder.todo.Models.data;

import javax.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.zachholder.todo.Models.ItemType;
import com.zachholder.todo.Models.User;

@Repository
@Transactional
public interface UserDao extends CrudRepository<User, Integer>{

}
