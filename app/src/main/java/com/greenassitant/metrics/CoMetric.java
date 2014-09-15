package com.greenassitant.metrics;

import android.graphics.Color;

import com.greenassitant.model.BasketItem;

import java.util.List;

public class CoMetric {

    private int[] colors = new int[]{0xff8ec343, 0xffffac34, 0xfff75e25};

    public long calculate(List<BasketItem> items) {
        long score = 0;
        for (BasketItem i : items) {
            score += calculate(i);
        }
        return score;
    }

    public long calculate(BasketItem item) {
        return (long) (item.getScore() * item.getCount());
    }

    public int colorForLevel(long score) {
        if (score < 1000) {
            return colors[0];
        } else if (score > 1000 && score < 5000) {
            return colors[1];
        } else {
            return colors[2];
        }
    }

    public int colorForItem(BasketItem item) {
        if (item.isInBasket()) {
            return colorForLevel((long) item.getScore());
        } else {
            return Color.GRAY;
        }
    }
}
