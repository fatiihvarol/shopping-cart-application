package com.example.trendyolcasestudy.service.promotion;

import com.example.trendyolcasestudy.model.cart.Cart;
import com.example.trendyolcasestudy.model.item.Item;
import com.example.trendyolcasestudy.model.promotion.DiscountCalculationResult;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class PromotionChooserTest {


    @Autowired
    PromotionChooser promotionChooser;

    private List<Item> items;

    @BeforeEach
    public void setUp() {
        items = new ArrayList<>();
    }

    @Test
    public void it_should_return_sameSellerPromotion_when_cart_items_sellers_same() {

        //SameSellerPromotion ID = 9909    //discount_rate=0.1

        //given

        items.add(new Item(1, 1, 1,1000,1));
        items.add( new Item(2, 2, 1,1000,1));
        items.add( new Item(3, 3, 1,1000,1));

        Cart cart = new Cart(items);

        //when
        DiscountCalculationResult result = promotionChooser.choosePromotion(cart);
        //then
        assertEquals(9909, result.getPromotionId());
        assertEquals(300.0, result.getTotalDiscount());
    }

    @Test
    public void it_should_return_categoryPromotion_when_cart_items_categories_same() {

        //categoryPromotion ID = 5676   //categoryPromotionTargetCategoryId=3003  //discount_rate=0.01

        //given
        items.add(new Item(1, 3003, 1,100000,1));
        items.add( new Item(2, 3003, 2,100000,1));
        items.add( new Item(3, 3003, 3,100000,1));

        Cart cart = new Cart(items);

        //when
        DiscountCalculationResult result = promotionChooser.choosePromotion(cart);
        //then
        assertEquals(5676, result.getPromotionId());
        assertEquals(15000.0, result.getTotalDiscount());
    }

    @Test
    public void it_should_return_totalPricePromotion() {

        //campaignPromotion ID = 1234   //campaignPromotionTargetCategoryId=3003  //discount_rate=0.01

        //given
        items.add(new Item(1, 1, 1,100,1));
        items.add( new Item(2, 1, 2,100,1));
        items.add( new Item(3, 1, 3,100,1));


        Cart cart = new Cart(items);

        //when
        DiscountCalculationResult result = promotionChooser.choosePromotion(cart);
        //then
        assertEquals(1232, result.getPromotionId());
        assertEquals(250.0, result.getTotalDiscount());
    }
}
