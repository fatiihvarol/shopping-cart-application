package com.example.trendyolcasestudy.service.promotion;

import com.example.trendyolcasestudy.model.cart.Cart;
import com.example.trendyolcasestudy.model.promotion.DiscountCalculationResult;
import com.example.trendyolcasestudy.model.promotion.TotalPromotion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TotalPromotionService implements Promotion {
    private final TotalPromotion totalPromotion;

    @Autowired
    public TotalPromotionService(TotalPromotion totalPromotion) {
        this.totalPromotion = totalPromotion;
    }

    @Override
    public DiscountCalculationResult calculateDiscountRate(Cart cart) {
        double totalDiscount = calculateDiscount(cart.getTotalPrice());
        return new DiscountCalculationResult(totalPromotion.getPROMOTION_ID(), totalDiscount);
    }

    public double calculateDiscount(double totalPrice) {
        List<Double> thresholds = new ArrayList<>(totalPromotion.getDiscountTiers().keySet());
        Collections.sort(thresholds);
        for (Double threshold : thresholds) {
            if (totalPrice < threshold) {
                return totalPromotion.getDiscountTiers().get(threshold);
            }
        }
        return 0.0;
    }

}
