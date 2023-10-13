package com.example.trendyolcasestudy.model.promotion;

public class DiscountCalculationResult {
    private final int promotionId;
    private final double totalDiscount;

    public DiscountCalculationResult(int promotionId, double totalDiscount) {
        this.promotionId = promotionId;
        this.totalDiscount = totalDiscount;
    }

    public int getPromotionId() {
        return promotionId;
    }

    public double getTotalDiscount() {
        return totalDiscount;
    }
}
