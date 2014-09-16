package com.greenassitant.adapter;

import com.greenassitant.model.BasketItem;

import java.util.Comparator;

public class BasketItemComparator implements Comparator<BasketItem> {
    @Override
    public int compare(BasketItem lhs, BasketItem rhs) {
        return new Boolean(rhs.isInBasket()).compareTo(new Boolean(lhs.isInBasket()));
    }
}
