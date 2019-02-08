package com.zachholder.todo.Models.data;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.zachholder.todo.Models.Aisle;

@Repository
@Transactional
public interface AisleDao extends CrudRepository<Aisle, Integer>{

	}