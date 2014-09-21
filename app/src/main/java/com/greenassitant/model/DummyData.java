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
        list.add(new BasketItem("Salmon", 15015, 0.5, "kg"));
        list.add(new BasketItem("Apples", 700, 1.0, "kg"));
        list.add(new BasketItem("Beef", 16230, 0.5, "kg"));
        list.add(new BasketItem("Flour", 730, 1.0, "kg"));
        list.add(new BasketItem("Chicken", 3760, 0.5, "kg"));
        list.add(new BasketItem("Potatoes", 310, 1.0, "kg"));
        list.add(new BasketItem("Milk", 1010, 0.4, "kg"));
        list.add(new BasketItem("Eggs", 2300, 0.4, "kg"));
        list.add(new BasketItem("Yogurt", 1020, 0.5, "kg"));
        list.add(new BasketItem("Bananas", 660, 1.0, "kg"));
        list.add(new BasketItem("Peach", 500, 1.0, "kg"));
        list.add(new BasketItem("Pork", 5700, 0.5, "kg"));
        list.add(new BasketItem("Beans", 980, 0.4, "kg"));
        list.add(new BasketItem("Tomato", 260, 1.0, "kg"));
        list.add(new BasketItem("Turkey", 4800, 0.4, "kg"));
        list.add(new BasketItem("Orange", 450, 1.0, "kg"));
        list.add(new BasketItem("Grapes", 4400, 1.0, "kg"));
        list.add(new BasketItem("Broccoli", 500, 1.0, "kg"));
        list.add(new BasketItem("Lettuce", 230, 1.0, "kg"));
        list.add(new BasketItem("Shrimp", 17300, 1.0, "kg"));
        list.add(new BasketItem("Rice", 310, 1.0, "kg"));
        list.add(new BasketItem("Almonds", 2250, 0.5, "kg"));
        list.add(new BasketItem("Peanuts", 1240, 0.5, "kg"));
        list.add(new BasketItem("Vegetable Oil", 1750, 1.0, "kg"));
        list.add(new BasketItem("Butter", 1370, 1.0, "kg"));
        list.add(new BasketItem("Tuna", 4300, 0.5, "kg"));
        list.add(new BasketItem("Strawberry", 3000, 0.5, "kg"));
        list.add(new BasketItem("Cabbage", 2600, 1.0, "kg"));
        list.add(new BasketItem("Carrot", 3100, 1.0, "kg"));
        list.add(new BasketItem("Kobe Beef", 800000, 1.0, "kg"));
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
