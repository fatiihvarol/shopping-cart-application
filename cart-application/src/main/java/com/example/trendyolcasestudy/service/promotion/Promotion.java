package com.example.trendyolcasestudy.service.promotion;

import com.example.trendyolcasestudy.model.cart.Cart;
import com.example.trendyolcasestudy.model.promotion.DiscountCalculationResult;

public interface Promotion {
    DiscountCalculationResult calculateDiscountRate(Cart cart);
}
