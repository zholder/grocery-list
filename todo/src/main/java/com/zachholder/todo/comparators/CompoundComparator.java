package com.zachholder.todo.comparators;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.zachholder.todo.Models.UserItem;

public class CompoundComparator implements Comparator<UserItem> {

    private List<Comparator<UserItem>> comparators = new ArrayList<>();

    public void add(Comparator<UserItem> item){
        comparators.add(item);
    }

    public int compare(UserItem o1, UserItem o2){
        String currentItem = o1.getAisle().getName();
        while (o2.getAisle().getName().equals(currentItem)){
            return o1.getName().compareTo(o2.getName());
        } if (!o2.getAisle().getName().equals(currentItem)){
        	currentItem = o2.getAisle().getName();
        } return o2.getAisle().getPosition() - o1.getAisle().getPosition();

    }

}