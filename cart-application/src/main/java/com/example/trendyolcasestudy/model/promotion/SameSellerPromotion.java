package com.example.trendyolcasestudy.model.promotion;

import org.springframework.stereotype.Component;

@Component
public class SameSellerPromotion {
    private final int PROMOTION_ID ;

    private final double DISCOUNT_RATE ;


    public SameSellerPromotion() {
        this.DISCOUNT_RATE = 0.1;
        this.PROMOTION_ID=9909;
    }


    public int getPROMOTION_ID() {
        return PROMOTION_ID;
    }

    public double getDISCOUNT_RATE() {
        return DISCOUNT_RATE;
    }
}
