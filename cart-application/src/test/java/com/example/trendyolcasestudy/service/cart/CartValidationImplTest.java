package com.example.trendyolcasestudy.service.cart;

import com.example.trendyolcasestudy.model.item.DigitalItem;
import com.example.trendyolcasestudy.model.item.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class CartValidationImplTest {
    private CartValidationImpl<Item> cartValidation;


    private List<Item> items;
    @BeforeEach
    public void setUp() {
        cartValidation = new CartValidationImpl<>();
        items = new ArrayList<>();
    }

    @Test
    void it_should_throw_exception_when_cart_items_not_unique() {

        //MAX UNIQUE ITEM ALLOWED IN THE CART = 10


        //given
        ReflectionTestUtils.setField(cartValidation, "uniqueItemCount", 10);


        items.add(new Item(1, 1, 1,1000,1));
        items.add(new Item(2, 1, 1,1000,1));
        items.add(new Item(3, 1, 1,1000,1));
        items.add(new Item(4, 1, 1,1000,1));
        items.add(new Item(5, 1, 1,1000,1));
        items.add(new Item(6, 1, 1,1000,1));
        items.add(new Item(7, 1, 1,1000,1));
        items.add(new Item(8, 1, 1,1000,1));
        items.add(new Item(9, 1, 1,1000,1));
        items.add(new Item(10, 1, 1,1000,1));
        items.add(new Item(11, 1, 1,1000,1));

        //when


        //then
        IllegalArgumentException exception
                = assertThrows(IllegalArgumentException.class, () -> cartValidation.isCartItemsUnique(items));
        assertEquals("Cart items are not unique", exception.getMessage());
    }

    @Test
    void it_should_not_throw_exception_when_items_are_unique(){

        //MAX UNIQUE ITEM ALLOWED IN THE CART = 10

        //given
        ReflectionTestUtils.setField(cartValidation, "uniqueItemCount", 10);

        items.add(new Item(1, 1, 1,1000,1));
        items.add(new Item(2, 1, 1,1000,1));
        items.add(new Item(3, 1, 1,1000,1));

        //when

        //then
        assertDoesNotThrow(() -> cartValidation.isCartItemsUnique(items));
    }

    @Test
    void it_should_throw_exception_when_cart_total_item_count_exceeds_limit() {

        //MAX TOTAL ITEM ALLOWED IN THE CART = 30


        //given
        ReflectionTestUtils.setField(cartValidation, "maxTotalItemCount", 30);

        items.add(new Item(1, 1, 1,1000,10));
        items.add(new Item(2, 1, 1,1000,10));
        items.add(new Item(3, 1, 1,1000,10));
        items.add(new Item(4, 1, 1,1000,1));

        //when


        //then
        IllegalArgumentException exception
                = assertThrows(IllegalArgumentException.class, () -> cartValidation.isCartTotalItemCountValid(items));
        assertEquals("Cart can not have more than 30 items", exception.getMessage());
    }

    @Test
    void it_should_not_throw_exception_when_cart_total_item_count_is_valid() {

        //MAX TOTAL ITEM ALLOWED IN THE CART = 30

        //given
        ReflectionTestUtils.setField(cartValidation, "maxTotalItemCount", 30);

        items.add(new Item(1, 1, 1,1000,10));
        items.add(new Item(2, 1, 1,1000,10));
        items.add(new Item(3, 1, 1,1000,10));

        //when

        //then
        assertDoesNotThrow(() -> cartValidation.isCartTotalItemCountValid(items));
    }

    @Test
    void it_should_throw_exception_when_cart_total_amount_exceeds_limit() {

        //MAX TOTAL CART AMOUNT 500.000


        //given
        ReflectionTestUtils.setField(cartValidation, "maxTotalCartAmount", new BigDecimal("500000"));

        items.add(new Item(1, 1, 1,100000,10));
        items.add(new Item(2, 1, 1,100000,10));
        items.add(new Item(3, 1, 1,100000,10));
        items.add(new Item(4, 1, 1,100000,10));
        items.add(new Item(5, 1, 1,100000,10));

        //when

        //then
        IllegalArgumentException exception
                = assertThrows(IllegalArgumentException.class, () -> cartValidation.isCartTotalAmountValid(items));

        assertEquals("Cart total amount can not exceed 500,000 TL", exception.getMessage());
    }
    @Test
    void it_should_not_throw_exception_when_cart_total_amount_is_valid() {

        //MAX TOTAL CART AMOUNT 500.000

        //given
        ReflectionTestUtils.setField(cartValidation, "maxTotalCartAmount", new BigDecimal("500000"));

        items.add(new Item(1, 1, 1,2000,1));

        //when

        //then
        assertDoesNotThrow(() -> cartValidation.isCartTotalAmountValid(items));
    }

    @Test
    void it_should_throw_exception_when_item_quantity_exceeds_limit() {

        //MAX ITEM QUANTITY ALLOWED IN THE CART = 10

        //given
        ReflectionTestUtils.setField(cartValidation, "itemExceedsLimit", 10);
        items.add(new Item(1, 1, 1,1000,11));

        //when

        //then
        IllegalArgumentException exception
                = assertThrows(IllegalArgumentException.class, () -> cartValidation.isItemQuantityValid(items));

        assertEquals("You can not add more than 10 items", exception.getMessage());

    }

    @Test
    void it_should_not_throw_exception_when_item_quantity_is_valid() {

        //MAX ITEM QUANTITY ALLOWED IN THE CART = 10

        //given
        ReflectionTestUtils.setField(cartValidation, "itemExceedsLimit", 10);
        items.add(new Item(1, 1, 1,1000,10));

        //when

        //then
        assertDoesNotThrow(() -> cartValidation.isItemQuantityValid(items));
    }

    @Test
    void it_should_throw_exception_when_item_quantity_exceeds_limit_for_digital_items() {

        //MAX ITEM QUANTITY ALLOWED IN THE CART FOR DIGITAL ITEMS = 5

        //given
        ReflectionTestUtils.setField(cartValidation, "itemExceedsLimitForDigitalItems", 5);
        items.add(new DigitalItem(1, 1, 1,1000,6));

        //when

        //then
        IllegalArgumentException exception
                = assertThrows(IllegalArgumentException.class, () -> cartValidation.isItemQuantityValidForDigitalItems(items));

        assertEquals("You can not add more than 5 digital items", exception.getMessage());

    }

    @Test
    void it_should_not_throw_exception_when_item_quantity_is_valid_for_digital_items() {

        //MAX ITEM QUANTITY ALLOWED IN THE CART FOR DIGITAL ITEMS = 5

        //given
        ReflectionTestUtils.setField(cartValidation, "itemExceedsLimitForDigitalItems", 5);
        items.add(new DigitalItem(1, 1, 1,1000,5));

        //when

        //then
        assertDoesNotThrow(() -> cartValidation.isItemQuantityValidForDigitalItems(items));
    }

    @Test
    void it_should_throw_exception_when_cart_has_digital_items_and_trying_add_no_digital_item() {

        //given
        items.add(new DigitalItem(1, 1, 1,1000,1));
        items.add(new Item(2, 1, 1,1000,1));
        //when

        //then
        IllegalArgumentException exception
                = assertThrows(IllegalArgumentException.class, () -> cartValidation.isCardValidForNoDigitalItems(items));

        assertEquals("You can add only digital items to cart", exception.getMessage());

    }

    @Test
    void it_should_not_throw_exception_when_cart_has_digital_items_and_trying_add_digital_item() {

        //given
        items.add(new Item(1, 1, 1,1000,1));
        items.add(new Item(2, 1, 1,1000,1));
        //when

        //then
        assertDoesNotThrow(() -> cartValidation.isCardValidForNoDigitalItems(items));
    }
}