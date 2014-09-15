package com.greenassitant.model;

import java.util.LinkedList;
import java.util.List;

public class DummyData {

    /**
     * Dummy data for search
     */
    public static List<BasketItem> getData() {
        LinkedList<BasketItem> list = new LinkedList<BasketItem>();
        list.add(new BasketItem("Cheese", 9860, 0.4, "kg"));
        list.add(new BasketItem("Peas", 930, 0.5, "kg"));
        list.add(new BasketItem("Salmon", 5015, 0.5, "kg"));
        list.add(new BasketItem("Apples", 200, 1.0, "kg"));
        list.add(new BasketItem("Beef", 16230, 0.5, "kg"));
        list.add(new BasketItem("Flour", 730, 1.0, "kg"));
        list.add(new BasketItem("Chicken", 3760, 0.5, "kg"));
        list.add(new BasketItem("Potatoes", 310, 1.0, "kg"));
        return list;
    }

    /**
     * Dummy search query
     */
    public static BasketItem getForName(String name) {

        // Search dummy products db
        for (BasketItem i : getData()) {
            if (i.getName().toLowerCase().startsWith(name.toLowerCase())) {
                return i;
            }
        }

        // If product not found, generate one with random values
        int greenScore = 1 + (int) (Math.random() * 3);
        return new BasketItem(name, greenScore, 0.1, "kg");
    }
}
