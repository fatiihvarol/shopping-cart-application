package com.example.trendyolcasestudy.service.cart;

import com.example.trendyolcasestudy.model.cart.Cart;
import com.example.trendyolcasestudy.model.item.Item;
import com.example.trendyolcasestudy.model.promotion.DiscountCalculationResult;
import com.example.trendyolcasestudy.repository.ItemRepository;
import com.example.trendyolcasestudy.response.ResponseService;
import com.example.trendyolcasestudy.service.promotion.PromotionChooser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import java.util.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CartServiceTest {

    @InjectMocks
    private CartService cartService;

    @Mock
    private Cart cart;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private PromotionChooser promotionChooser;

    @Mock
    private ResponseService responseService;



    @Test
    public void it_should_return_empty_cart_when_cart_has_not_any_item() {
        // Arrange
        when(itemRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        Cart result = cartService.displayCart();

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(0.0, result.getTotalPrice());
        Assertions.assertEquals(0.0, result.getTotalDiscount());
        Assertions.assertEquals(0, result.getDiscountId());
        Assertions.assertNull(result.getItems());

    }

    @Test
    public void it_should_return_total_price_zero_when_discount_is_greater_than_products_price() {
        //given
        List<Item> mockItems = new ArrayList<>();
        Item item = new Item(1, 1, 1, 100.0, 1);
        mockItems.add(item);
        when(itemRepository.findAll()).thenReturn(mockItems);

        Cart mockCart = new Cart();
        when(promotionChooser.choosePromotion(any())).thenReturn(new DiscountCalculationResult(1232,250)); // promotionChooser.choosePromotion() çağrıldığında boş bir DiscountCalculationResult nesnesi döndür
        //when
        Cart result = cartService.displayCart();


        //then

        verify(itemRepository, times(1)).findAll();
        verify(promotionChooser, times(1)).choosePromotion(any());
        Assertions.assertNotNull(result);
        Assertions.assertEquals(0.0, result.getTotalPrice());
        Assertions.assertEquals(100.0, result.getTotalDiscount());
        Assertions.assertEquals(1232, result.getDiscountId());



    }

    @Test
    public void it_should_set_cart_properties_when_give_cart_and_discount() {
        DiscountCalculationResult discountCalculationResult = new DiscountCalculationResult(1232, 250.0);

        when(cart.getTotalPrice()).thenReturn(1000.0);

        cartService.updatePriceWithDiscount(cart, discountCalculationResult);

        //then
        verify(cart).setTotalPrice(750.0);
        verify(cart).setTotalDiscount(250.0);
        verify(cart).setDiscountId(1232);

    }


    @Test
    public void testResetCart() {
        // given
        when(responseService.createApiResponse(true, "Your cart is empty now")).thenReturn(ResponseEntity.ok(Map.of("result", true, "message", "Your cart is empty now")));
        // when

        ResponseEntity<Map<String, Object>> result = cartService.resetCart();

        // then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(true, result.getBody().get("result"));
        Assertions.assertEquals("Your cart is empty now", result.getBody().get("message"));
        verify(itemRepository, times(1)).deleteAll();


    }



    @Test
    public void calculateNewTotalPrice_function_should_return_new_price_after_discount() {
        // Arrange
        double totalPrice = 100.0;
        double totalDiscount = 20.0;
        double expectedNewTotalPrice = 80.0;

        // Act
        double newTotalPrice = cartService.calculateNewTotalPrice(totalPrice, totalDiscount);

        // Assert
        Assertions.assertEquals(expectedNewTotalPrice, newTotalPrice, 0.001);
    }

    @Test
    public void CalculateNewTotalPrice_function_should_return_new_price_after_0_discount() {
        // Arrange
        double totalPrice = 100.0;
        double totalDiscount = 0.0;
        double expectedNewTotalPrice = 100.0;

        // Act
        double newTotalPrice = cartService.calculateNewTotalPrice(totalPrice, totalDiscount);

        // Assert
        Assertions.assertEquals(expectedNewTotalPrice, newTotalPrice, 0.001);
    }



}
