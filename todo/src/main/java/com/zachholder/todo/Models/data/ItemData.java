package com.zachholder.todo.Models.data;

import java.util.ArrayList;

import com.zachholder.todo.Models.UserItem;

public class ItemData {
    static ArrayList<UserItem> items = new ArrayList<>();

	public static ArrayList<UserItem> getItems() {
		return items;
	}

	public static void add(UserItem item) {
		items.add(item);
	}

    public static void remove(int id) {
        UserItem itemToRemove = getById(id);
        items.remove(itemToRemove);
    }

    // getById
    public static UserItem getById(int id) {

        UserItem theItem = null;

        for (UserItem candidateItem : items) {
            if (candidateItem.getId() == id) {
            	theItem = candidateItem;
            }
        }

        return theItem;
    }
   
}
