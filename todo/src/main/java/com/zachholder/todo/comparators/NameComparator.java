package com.zachholder.todo.comparators;

import java.util.Comparator;

import com.zachholder.todo.Models.UserItem;

public class NameComparator implements Comparator<UserItem> {
		
		@Override
	    public int compare(UserItem o1, UserItem o2) {
	        return o1.getName().compareTo(o2.getName());
	    }
}
