package com.greenassitant.metrics;

public class TreeMetric {
    public double calculate(double co2Score) {
        return 0.7 + (co2Score / 200000);
    }
}
