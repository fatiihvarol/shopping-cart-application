package com.example.trendyolcasestudy.service.item;

import com.example.trendyolcasestudy.model.item.DefaultItem;
import com.example.trendyolcasestudy.model.item.Item;
import com.example.trendyolcasestudy.repository.ItemRepository;
import com.example.trendyolcasestudy.response.ResponseService;
import com.example.trendyolcasestudy.service.cart.CartValidation;
import com.example.trendyolcasestudy.service.item.DefaultItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class DefaultItemServiceTest {

    @InjectMocks
    private DefaultItemService defaultItemService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private CartValidation<Item> cartValidation;

    @Mock
    private ResponseService responseService;

private List<Item> items;
    @BeforeEach
    void setUp() {
        responseService = mock(ResponseService.class);
        defaultItemService = new DefaultItemService(itemRepository, cartValidation, responseService);


    }
    @Test
    void it_should_return_all_items_when_getAllItems_called() {
        // given
        List<Item> mockItems = new ArrayList<>();
        when(itemRepository.findAll()).thenReturn(mockItems);

        // when
        List<Item> result = defaultItemService.getAllItems();

        //then
        assertEquals(mockItems, result);
    }


    @Test
    void it_should_save_item_and_return_response_as_ok_when_give_valid_default_item() {
       //given
        doNothing().when(cartValidation).isCartItemsUnique(anyList());
        doNothing().when(cartValidation).isCartTotalAmountValid(anyList());
        doNothing().when(cartValidation).isCartTotalItemCountValid(anyList());
        doNothing().when(cartValidation).isItemQuantityValid(anyList());

        when(responseService.createApiResponse(eq(true), anyString()))
                .thenReturn(new ResponseEntity<>(Map.of("success", true, "message", "Item added to cart"), HttpStatus.OK));

        //when
        ResponseEntity<Map<String, Object>> response = defaultItemService.addItemToCart(new DefaultItem(1, 1, 1, 1, 1, new ArrayList<>()));

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Map.of("success", true, "message", "Item added to cart"), response.getBody());
    }

   @Test
   void it_should_save_item_when_item_does_not_exist_in_cart() {
       //given
       DefaultItem item = new DefaultItem(1, 1, 1, 1, 1, new ArrayList<>());
       when(itemRepository.findAll()).thenReturn(emptyList());
       when(itemRepository.save(item)).thenReturn(item);
       when(responseService.createApiResponse(eq(true), anyString()))
               .thenReturn(new ResponseEntity<>(Map.of("success", true, "message", "Item added to cart"), HttpStatus.OK));

       //when
       ResponseEntity<Map<String, Object>> response = defaultItemService.saveItem(item);

       //then
       assertEquals(HttpStatus.OK, response.getStatusCode());
       assertEquals(Map.of("success", true, "message", "Item added to cart"), response.getBody());
    }
    @Test
    void it_should_update_quantity_and_save_item_when_item_exist_in_cart(){
        //given
        DefaultItem item = new DefaultItem(1, 1, 1, 1, 1, new ArrayList<>());
        DefaultItem itemToUpdate = new DefaultItem(1, 1, 1, 1, 1, new ArrayList<>());
        itemToUpdate.setQuantity(item.getQuantity()+item.getQuantity());
        when(itemRepository.findAll()).thenReturn(List.of(item));
        when(itemRepository.save(itemToUpdate)).thenReturn(itemToUpdate);
        when(responseService.createApiResponse(eq(true), anyString()))
                .thenReturn(new ResponseEntity<>(Map.of("success", true, "message", "Item added to cart"), HttpStatus.OK));

        //when
        ResponseEntity<Map<String, Object>> response = defaultItemService.saveItem(item);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Map.of("success", true, "message", "Item added to cart"), response.getBody());
    }

    @Test
    void it_should_return_response_as_ok_when_give_exist_item() {
        //given
        when(itemRepository.findItemByItemIdforDelete(anyInt()))
                .thenReturn(Collections
                        .singletonList(new DefaultItem(1, 1, 1, 1, 1, new ArrayList<>())));

        when(responseService.createApiResponse(eq(true), anyString()))
                .thenReturn(new ResponseEntity<>(Map.of("success", true, "message", "Item deleted from cart"), HttpStatus.OK));

        //when
        ResponseEntity<Map<String, Object>> response = defaultItemService.deleteItemFromCart(1);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Map.of("success", true, "message", "Item deleted from cart"), response.getBody());
    }
    @Test
    void it_should_return_response_as_false_when_given_item_no_exist() {
        //given
        when(itemRepository.findItemByItemIdforDelete(anyInt())).thenReturn(Collections.emptyList());

        when(responseService.createApiResponse(eq(false), eq("Item not found")))
                .thenReturn(new ResponseEntity<>(Map.of("success", false, "message", "Item not found"), HttpStatus.OK));

        //when
        ResponseEntity<Map<String, Object>> response = defaultItemService.deleteItemFromCart(1);

        //then

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Map.of("success", false, "message", "Item not found"), response.getBody());
    }


}
