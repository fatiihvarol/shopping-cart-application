package com.example.trendyolcasestudy.model.promotion;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TotalPromotion {
    private final int PROMOTION_ID = 1232;
    private final Map<Double, Double> discountTiers = new HashMap<>();

    public TotalPromotion() {

        discountTiers.put(5000.0, 250.0);
        discountTiers.put(10000.0, 500.0);
        discountTiers.put(50000.0, 1000.0);
        discountTiers.put(Double.MAX_VALUE, 2000.0);
    }

    public int getPROMOTION_ID() {
        return PROMOTION_ID;
    }

    public Map<Double, Double> getDiscountTiers() {
        return discountTiers;
    }
}
