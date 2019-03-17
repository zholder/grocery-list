package com.zachholder.todo.comparators;

import java.util.Comparator;

import com.zachholder.todo.Models.UserItem;

public class AisleComparator implements Comparator<UserItem> {

    @Override
    public int compare(UserItem o1, UserItem o2) {
        return o2.getAisle().getPosition() - o1.getAisle().getPosition();
    }
}
