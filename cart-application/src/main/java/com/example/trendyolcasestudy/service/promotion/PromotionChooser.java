package com.example.trendyolcasestudy.service.promotion;

import com.example.trendyolcasestudy.model.cart.Cart;
import com.example.trendyolcasestudy.model.promotion.DiscountCalculationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class PromotionChooser {


    private final CategoryPromotionService categoryPromotionService;
    private final SameSellerPromotionService sameSellerPromotionService;

    private final TotalPromotionService totalPromotionService;
    @Autowired
    public PromotionChooser(CategoryPromotionService categoryPromotionService, SameSellerPromotionService sameSellerPromotionService, TotalPromotionService totalPromotionService) {

        this.categoryPromotionService = categoryPromotionService;
        this.sameSellerPromotionService = sameSellerPromotionService;
        this.totalPromotionService = totalPromotionService;
    }




    public DiscountCalculationResult choosePromotion(Cart cart) {

            List<DiscountCalculationResult> allDiscounts =
                    List.of(categoryPromotionService.calculateDiscountRate(cart),
                            sameSellerPromotionService.calculateDiscountRate(cart),
                            totalPromotionService.calculateDiscountRate(cart));


        Optional<DiscountCalculationResult> maxDiscount = allDiscounts.stream()
                .max(Comparator.comparingDouble(DiscountCalculationResult::getTotalDiscount));
                return maxDiscount.orElse(new DiscountCalculationResult(0, 0));

    }
}
