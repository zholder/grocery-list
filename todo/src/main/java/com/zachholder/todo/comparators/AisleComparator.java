package com.zachholder.todo.comparators;

import java.util.Comparator;

import com.zachholder.todo.Models.Item;

public class AisleComparator implements Comparator<Item> {

    @Override
    public int compare(Item o1, Item o2) {
        return o2.getAisle().getPosition() - o1.getAisle().getPosition();
    }
}
