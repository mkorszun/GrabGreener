package com.greenassitant.model;

import java.io.Serializable;

public class BasketItem implements Serializable {

    private String name;
    private String unit;

    private double count;
    private double score;

    private boolean inBasket = true;

    public BasketItem(String name, double score, double count, String unit) {
        this.name = name;
        this.score = score;
        this.count = count;
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public boolean isInBasket() {
        return inBasket;
    }

    public void setInBasket(boolean inBasket) {
        this.inBasket = inBasket;
    }

    public double getCount() {
        return count;
    }

    public void setCount(double count) {
        this.count = count;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getQuantity() {
        return String.format("%.1f %s", getCount(), getUnit());
    }
}
