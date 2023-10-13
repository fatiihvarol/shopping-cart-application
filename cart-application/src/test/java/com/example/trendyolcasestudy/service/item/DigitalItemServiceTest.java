package com.example.trendyolcasestudy.service.item;

import com.example.trendyolcasestudy.model.item.DigitalItem;
import com.example.trendyolcasestudy.model.item.Item;
import com.example.trendyolcasestudy.model.item.VasItem;
import com.example.trendyolcasestudy.repository.DigitalItemRepository;
import com.example.trendyolcasestudy.repository.ItemRepository;
import com.example.trendyolcasestudy.response.ResponseService;
import com.example.trendyolcasestudy.service.cart.CartValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class DigitalItemServiceTest {

    @InjectMocks
    private DigitalItemService digitalItemService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private CartValidation<DigitalItem> cartValidation;

    @Mock
    private ResponseService responseService;

    private List<Item> items;

    @Mock
    private DigitalItemRepository digitalItemRepository;

    @BeforeEach
    void setUp() {
        responseService = mock(ResponseService.class);
        digitalItemService = new DigitalItemService(digitalItemRepository, itemRepository, cartValidation, responseService);

    }

    @Test
    void it_should_return_all_items_when_getAllItems_called() {
        // given
        List<Item> mockItems = List.of(new DigitalItem(1, 1, 1, 1,1),
                                        new VasItem(1, 1, 1, 1,1,1));
        when(itemRepository.findAll()).thenReturn(mockItems);

        // when
        List<Item> result = digitalItemService.getAllItems();

        //then
        assertEquals(mockItems, result);
    }

    @Test
    void it_should_return_all_digital_items_when_getAllDigitalItems_called() {
        // given
        List<DigitalItem> mockItems = List.of(new DigitalItem(1, 7889, 1, 1,1));
        when(digitalItemRepository.findAll()).thenReturn(mockItems);

        // when
        List<DigitalItem> result = digitalItemService.getAllIDigitalItems();

        //then
        assertEquals(mockItems, result);
    }

    @Test
    void it_should_delete_item_when_item_exist_in_cart() {
        // given
        DigitalItem digitalItem = new DigitalItem(1, 7889, 1, 1,1);
        when(digitalItemRepository.findItemByItemId(1)).thenReturn(java.util.Optional.of(digitalItem));
        when(responseService.createApiResponse(eq(true), anyString()))
                .thenReturn(new ResponseEntity<>(Map.of("success", true, "message", "Item deleted from cart"), HttpStatus.OK));

        // when
        var result = digitalItemService.deleteItemFromCart(1);

        //then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(Map.of("success", true, "message", "Item deleted from cart"), result.getBody());

    }
    @Test
    void it_should_not_delete_item_when_item_does_not_exist_in_cart() {
        // given
        when(digitalItemRepository.findItemByItemId(1)).thenReturn(java.util.Optional.empty());
        when(responseService.createApiResponse(eq(false), anyString()))
                .thenReturn(new ResponseEntity<>(Map.of("success", false, "message", "Item not found"), HttpStatus.OK));

        // when
        var result = digitalItemService.deleteItemFromCart(1);

        //then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(Map.of("success", false, "message", "Item not found"), result.getBody());

    }

    @Test
    void it_should_throw_exception_when_digital_item_category_id_is_not_valid() {
        //Digital item category id must be 7889

        // given
        ReflectionTestUtils.setField(digitalItemService, "digitalItemDefaultCategoryId", 7889);
        DigitalItem digitalItem = new DigitalItem(1, 1, 1, 1,1);
        // when
        // then
        assertThrows(IllegalArgumentException.class, () -> digitalItemService.isDigitalItemCategoryIdValid(digitalItem));

    }

    @Test
    void it_should_does_not_throw_exception_when_digital_item_category_id_is_valid() {

        //Digital item category id must be 7889

        ReflectionTestUtils.setField(digitalItemService, "digitalItemDefaultCategoryId", 7889);
        // given
        DigitalItem digitalItem = new DigitalItem(1, 7889, 1, 1,1);
        // when
        // then
        assertDoesNotThrow(() -> digitalItemService.isDigitalItemCategoryIdValid(digitalItem));

    }

    @Test
    void it_should_save_item_and_return_true_when_saveItem_called(){
        //given

        DigitalItem digitalItem = new DigitalItem(1, 7889, 1, 1,1);
        when(itemRepository.findAll()).thenReturn(List.of(digitalItem));
        when(responseService.createApiResponse(eq(true), anyString()))
                .thenReturn(new ResponseEntity<>(Map.of("success", true, "message", "Item added to cart"), HttpStatus.OK));

        //when
        ResponseEntity<Map<String, Object>> response = digitalItemService.saveItem(digitalItem);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Map.of("success", true, "message", "Item added to cart"), response.getBody());
    }
    @Test
    void it_should_throw_exception_when_cart_has_no_digital_items(){
        //given
        Item item = new Item(1, 1, 1, 1,1);
        when(itemRepository.findAll()).thenReturn(List.of(item));
        when(responseService.createApiResponse(eq(false), anyString()))
                .thenReturn(new ResponseEntity<>(Map.of("success", false, "message", "Cart has no digital items"), HttpStatus.OK));

        //when

        //then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> digitalItemService.isCartHasOnlyDigitalItems());
        assertEquals("Cart can only have digital items", exception.getMessage());

    }
    @Test
    void it_should_does_not_throw_exception_when_cart_has_only_digital_items(){
        //given
        DigitalItem digitalItem = new DigitalItem(1, 7889, 1, 1,1);
        when(itemRepository.findAll()).thenReturn(List.of(digitalItem));
        when(responseService.createApiResponse(eq(true), anyString()))
                .thenReturn(new ResponseEntity<>(Map.of("success", true, "message", "Cart has no digital items"), HttpStatus.OK));

        //when

        //then
        assertDoesNotThrow(() -> digitalItemService.isCartHasOnlyDigitalItems());

    }
    @Test
    void it_should_add_item_when_give_valid_digital_item() {
        //Digital item category id must be 7889

        //given


        ReflectionTestUtils.setField(digitalItemService, "digitalItemDefaultCategoryId", 7889);

        DigitalItem digitalItem = new DigitalItem(1, 7889, 1, 1,1);
        when(itemRepository.findAll()).thenReturn(List.of(digitalItem));
        when(responseService.createApiResponse(eq(true), anyString()))
                .thenReturn(new ResponseEntity<>(Map.of("success", true, "message", "Item added to cart"), HttpStatus.OK));

        //when
        ResponseEntity<Map<String, Object>> response = digitalItemService.addItemToCart(digitalItem);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Map.of("success", true, "message", "Item added to cart"), response.getBody());
    }
}