package com.zachholder.todo.comparators;

import java.util.Comparator;

import com.zachholder.todo.Models.Item;

public class NameComparator implements Comparator<Item> {
		
		@Override
	    public int compare(Item o1, Item o2) {
	        return o1.getName().compareTo(o2.getName());
	    }
}
