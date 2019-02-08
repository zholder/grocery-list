package com.zachholder.todo.comparators;

import java.util.Comparator;

import com.zachholder.todo.Models.Item;

public class TypeComparator implements Comparator<Item> {
	
	@Override
    public int compare(Item o1, Item o2) {
        return o1.getType().getName().compareTo(o2.getType().getName());
    }

}
