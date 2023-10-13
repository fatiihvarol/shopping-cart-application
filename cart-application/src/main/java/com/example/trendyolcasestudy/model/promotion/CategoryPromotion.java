package com.example.trendyolcasestudy.model.promotion;

import org.springframework.stereotype.Component;

@Component
public class CategoryPromotion {
    private final int PROMOTION_ID ;
    private final int TARGET_CATEGORY_ID ;
    private final double DISCOUNT_RATE ;

    public CategoryPromotion() {
        this.PROMOTION_ID = 5676;
        this.TARGET_CATEGORY_ID = 3003;
        this.DISCOUNT_RATE = 0.05;
    }

    public int getPROMOTION_ID() {
        return PROMOTION_ID;
    }

    public int getTARGET_CATEGORY_ID() {
        return TARGET_CATEGORY_ID;
    }

    public double getDISCOUNT_RATE() {
        return DISCOUNT_RATE;
    }
}
