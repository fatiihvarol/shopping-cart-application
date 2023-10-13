package com.example.trendyolcasestudy.service.promotion;

import com.example.trendyolcasestudy.model.cart.Cart;
import com.example.trendyolcasestudy.model.item.Item;
import com.example.trendyolcasestudy.model.promotion.CategoryPromotion;
import com.example.trendyolcasestudy.model.promotion.DiscountCalculationResult;
import com.example.trendyolcasestudy.service.promotion.CategoryPromotionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class CategoryPromotionServiceTest {

    @Mock
    private CategoryPromotion categoryPromotion;

    private CategoryPromotionService categoryPromotionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        categoryPromotionService = new CategoryPromotionService(categoryPromotion);
    }

    @Test
     void it_should_return_correct_discount_rate_when_cart_has_target_categoryId() {
        // TARGET_CATEGORY_ID = 3330   //DISCOUNT_RATE = 0.05  //PROMOTION_ID = 5676

        //given
        when(categoryPromotion.getTARGET_CATEGORY_ID()).thenReturn(3330);
        when(categoryPromotion.getDISCOUNT_RATE()).thenReturn(0.05);
        when(categoryPromotion.getPROMOTION_ID()).thenReturn(5676);

        List<Item> items = new ArrayList<>();
        items.add(new Item(1, 3330, 1, 100.0, 1));
        items.add(new Item(2, 1, 2, 100.0, 1));
        Cart cart = new Cart(items);

        //when
        DiscountCalculationResult discountCalculationResult = categoryPromotionService.calculateDiscountRate(cart);

        //then
        assertEquals( 5676, discountCalculationResult.getPromotionId());
        assertEquals(5.0, discountCalculationResult.getTotalDiscount());
    }
    @Test
     void it_should_return_0_when_given_non_target_categoryID() {
        // TARGET_CATEGORY_ID = 3330   //DISCOUNT_RATE = 0.05  //PROMOTION_ID = 5676

        //given
        when(categoryPromotion.getTARGET_CATEGORY_ID()).thenReturn(3330);
        when(categoryPromotion.getDISCOUNT_RATE()).thenReturn(0.05);
        when(categoryPromotion.getPROMOTION_ID()).thenReturn(5676);

        List<Item> items = new ArrayList<>();
        items.add(new Item(1, 1, 1, 100.0, 1));
        items.add(new Item(2, 1, 2, 100.0, 1));
        Cart cart = new Cart(items);

        //when
        DiscountCalculationResult discountCalculationResult = categoryPromotionService.calculateDiscountRate(cart);

        //then
        assertEquals( 5676, discountCalculationResult.getPromotionId());
        assertEquals(0.0, discountCalculationResult.getTotalDiscount());
    }
}
