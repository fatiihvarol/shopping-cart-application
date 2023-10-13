package com.example.trendyolcasestudy.service.promotion;

import com.example.trendyolcasestudy.model.cart.Cart;
import com.example.trendyolcasestudy.model.item.DefaultItem;
import com.example.trendyolcasestudy.model.item.Item;
import com.example.trendyolcasestudy.model.item.VasItem;
import com.example.trendyolcasestudy.model.promotion.DiscountCalculationResult;
import com.example.trendyolcasestudy.model.promotion.SameSellerPromotion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SameSellerPromotionService implements Promotion {
    private final SameSellerPromotion sameSellerPromotion;


    @Autowired
    public SameSellerPromotionService(SameSellerPromotion sameSellerPromotion) {
        this.sameSellerPromotion = sameSellerPromotion;

    }
    @Override
    public DiscountCalculationResult calculateDiscountRate(Cart cart){
        boolean isSameSeller= cart.getItems()
                .stream()
                .filter(item -> !(item instanceof VasItem))
                .map(Item::getSellerId)
                .distinct()
                .count()==1;

        if (isSameSeller) {

            double totalDiscount = cart.getItems().stream()
                    .mapToDouble(item -> (item.getPrice() * item.getQuantity()) * sameSellerPromotion.getDISCOUNT_RATE())
                    .sum();


            return new DiscountCalculationResult(sameSellerPromotion.getPROMOTION_ID(), totalDiscount);
        }

        return new DiscountCalculationResult(sameSellerPromotion.getPROMOTION_ID(), 0);








    }
}
