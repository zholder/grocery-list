package com.zachholder.todo.Models.data;

import java.util.ArrayList;

import com.zachholder.todo.Models.Item;

public class ItemData {
    static ArrayList<Item> items = new ArrayList<>();

	public static ArrayList<Item> getItems() {
		return items;
	}

	public static void add(Item item) {
		items.add(item);
	}

    public static void remove(int id) {
        Item itemToRemove = getById(id);
        items.remove(itemToRemove);
    }

 
    // getById
    public static Item getById(int id) {

        Item theItem = null;

        for (Item candidateItem : items) {
            if (candidateItem.getId() == id) {
            	theItem = candidateItem;
            }
        }

        return theItem;
    }
   
}
