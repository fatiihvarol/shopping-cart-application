package com.example.trendyolcasestudy.service.promotion;

import com.example.trendyolcasestudy.model.cart.Cart;
import com.example.trendyolcasestudy.model.promotion.CategoryPromotion;
import com.example.trendyolcasestudy.model.promotion.DiscountCalculationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryPromotionService implements Promotion {
    private final CategoryPromotion categoryPromotion;

    @Autowired
    public CategoryPromotionService(CategoryPromotion categoryPromotion) {
        this.categoryPromotion = categoryPromotion;

    }
    @Override
    public DiscountCalculationResult calculateDiscountRate(Cart cart){
        double totalDiscount = cart.getItems().stream()
                .filter(item -> (item.getCategoryId() == categoryPromotion.getTARGET_CATEGORY_ID()))
                .mapToDouble(item -> (item.getPrice() * item.getQuantity())* categoryPromotion.getDISCOUNT_RATE())
                .sum();


        return new DiscountCalculationResult(categoryPromotion.getPROMOTION_ID(), totalDiscount);
    }

}
