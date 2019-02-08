package com.zachholder.todo.comparators;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.zachholder.todo.Models.Item;

public class CompoundComparator implements Comparator<Item> {

    private List<Comparator<Item>> comparators = new ArrayList<>();

    public void add(Comparator<Item> item){
        comparators.add(item);
    }

    public int compare(Item o1, Item o2){
        String currentItem = o1.getAisle().getName();
        while (o2.getAisle().getName().equals(currentItem)){
            return o1.getName().compareTo(o2.getName());
        } if (!o2.getAisle().getName().equals(currentItem)){
        	currentItem = o2.getAisle().getName();
        } return o2.getAisle().getPosition() - o1.getAisle().getPosition();

    }

}