package com.example.trendyolcasestudy.service.promotion;

import com.example.trendyolcasestudy.model.cart.Cart;
import com.example.trendyolcasestudy.model.item.Item;
import com.example.trendyolcasestudy.model.promotion.DiscountCalculationResult;
import com.example.trendyolcasestudy.model.promotion.TotalPromotion;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class TotalPromotionServiceTest {

    @Mock
    private TotalPromotion totalPromotion;

    private TotalPromotionService totalPromotionService;

    private List<Item> items;

    private static Map<Double, Double> discountTiers;

    @BeforeAll
    public static void setUpDiscountTiers() {
        discountTiers = new HashMap<>();
        discountTiers.put(5000.0, 250.0);
        discountTiers.put(10000.0, 500.0);
        discountTiers.put(50000.0, 1000.0);
        discountTiers.put(Double.MAX_VALUE, 2000.0);
    }

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        totalPromotionService = new TotalPromotionService(totalPromotion);
        items = new ArrayList<>();
    }

    @Test
    public void it_should_apply_500_discount_when_total_price_below_5k() {
        // given
        items.add(new Item(1, 1, 1, 1000, 1));

        Cart cart = new Cart(items);

        when(totalPromotion.getDiscountTiers()).thenReturn(discountTiers);
        when(totalPromotion.getPROMOTION_ID()).thenReturn(1232);

        // when
        DiscountCalculationResult discountCalculationResult = totalPromotionService.calculateDiscountRate(cart);

        // then
        assertEquals(250.0, discountCalculationResult.getTotalDiscount());
        assertEquals(1232, discountCalculationResult.getPromotionId());
    }

    @Test
    public void it_should_apply_500_discount_when_total_price_between_5k_10k() {
        // given
        items.add(new Item(1, 1, 1, 2000, 4));


        Cart cart = new Cart(items);

        when(totalPromotion.getDiscountTiers()).thenReturn(discountTiers);
        when(totalPromotion.getPROMOTION_ID()).thenReturn(1232);

        // when
        DiscountCalculationResult discountCalculationResult = totalPromotionService.calculateDiscountRate(cart);

        // then
        assertEquals(500.0, discountCalculationResult.getTotalDiscount());
        assertEquals(1232, discountCalculationResult.getPromotionId());
    }
    @Test
    public void it_should_apply_1000_discount_when_total_price_between_10k_50k() {
        //given
        items.add(new Item(1, 1, 1, 5000, 4));

        Cart cart = new Cart(items);

        when(totalPromotion.getDiscountTiers()).thenReturn(discountTiers);
        when(totalPromotion.getPROMOTION_ID()).thenReturn(1232);

        //when
        DiscountCalculationResult discountCalculationResult = totalPromotionService.calculateDiscountRate(cart);

        // then
        assertEquals(1000.0, discountCalculationResult.getTotalDiscount());
        assertEquals(1232, discountCalculationResult.getPromotionId());
    }

    @Test
    public void it_should_apply_2000_discount_when_total_price_over_then_50k() {

        //given
        items.add(new Item(1, 1, 1, 20000, 3));

        Cart cart = new Cart(items);

        when(totalPromotion.getDiscountTiers()).thenReturn(discountTiers);
        when(totalPromotion.getPROMOTION_ID()).thenReturn(1232);

        //when
        DiscountCalculationResult discountCalculationResult = totalPromotionService.calculateDiscountRate(cart);

        // then
        assertEquals(2000.0, discountCalculationResult.getTotalDiscount());
        assertEquals(1232, discountCalculationResult.getPromotionId());
    }
}
