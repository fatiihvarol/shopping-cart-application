package com.example.trendyolcasestudy.service.promotion;

import com.example.trendyolcasestudy.model.cart.Cart;
import com.example.trendyolcasestudy.model.item.Item;
import com.example.trendyolcasestudy.model.promotion.CategoryPromotion;
import com.example.trendyolcasestudy.model.promotion.DiscountCalculationResult;
import com.example.trendyolcasestudy.model.promotion.SameSellerPromotion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class SameSellerPromotionServiceTest {
    @Mock
    private SameSellerPromotion sameSellerPromotion;

    private SameSellerPromotionService sameSellerPromotionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        sameSellerPromotionService = new SameSellerPromotionService(sameSellerPromotion);
    }
    @Test
    public void it_should_calculate_discount_rate_when_all_items_have_the_same_seller() {
        //DISCOUNT_RATE = 0.1  //PROMOTION_ID = 9909

        //given
        when(sameSellerPromotion.getDISCOUNT_RATE()).thenReturn(0.1);
        when(sameSellerPromotion.getPROMOTION_ID()).thenReturn(9909);

        List<Item> items = new ArrayList<>();
        items.add(new Item(1, 1, 9999, 100.0, 1));
        items.add(new Item(2, 1, 9999, 100.0, 1));
        Cart cart = new Cart(items);

        // When
        DiscountCalculationResult discountCalculationResult = sameSellerPromotionService.calculateDiscountRate(cart);




        // Then
        assertEquals(9909, discountCalculationResult.getPromotionId());
        assertEquals(20.0, discountCalculationResult.getTotalDiscount());
    }

    @Test
    public void it_should_calculate_discount_rate_as_0_when_items_have_different_sellers() {
        //DISCOUNT_RATE = 0.1  //PROMOTION_ID = 9909

        //given
        when(sameSellerPromotion.getDISCOUNT_RATE()).thenReturn(0.1);
        when(sameSellerPromotion.getPROMOTION_ID()).thenReturn(9909);

        List<Item> items = new ArrayList<>();
        items.add(new Item(1, 1, 9999, 100.0, 1));
        items.add(new Item(2, 1, 8888, 100.0, 1));
        Cart cart = new Cart(items);

        // When
        DiscountCalculationResult discountCalculationResult = sameSellerPromotionService.calculateDiscountRate(cart);




        // Then
        assertEquals(9909, discountCalculationResult.getPromotionId());
        assertEquals(0.0, discountCalculationResult.getTotalDiscount());
    }

}