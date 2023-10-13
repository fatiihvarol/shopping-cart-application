package com.example.trendyolcasestudy.service.item;

import com.example.trendyolcasestudy.model.item.DefaultItem;
import com.example.trendyolcasestudy.model.item.DigitalItem;
import com.example.trendyolcasestudy.model.item.Item;
import com.example.trendyolcasestudy.model.item.VasItem;
import com.example.trendyolcasestudy.repository.ItemRepository;
import com.example.trendyolcasestudy.repository.VasItemRepository;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
class VasItemServiceTest {

    @InjectMocks
    private VasItemService vasItemService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private CartValidation<Item> cartValidation;

    @Mock
    private VasItemRepository vasItemRepository;

    @Mock
    private ResponseService responseService;

    @BeforeEach
    void setUp() {
        responseService = mock(ResponseService.class);
        vasItemService = new VasItemService(vasItemRepository, itemRepository, cartValidation, responseService);

    }

    @Test
    void it_should_return_all_items_when_getAllItems_called() {
        // given
        List<Item> mockItems = List.of(new DigitalItem(1, 1, 1, 1,1),
                new VasItem(1, 1, 1, 1,1,1));
        when(itemRepository.findAll()).thenReturn(mockItems);

        // when
        List<Item> result = vasItemService.getAllItems();

        //then
        assertEquals(mockItems, result);

    }


    @Test
    void it_should_return_all_vas_items_when_getAllVasItems_called() {
        // given
        List<VasItem> mockItems = List.of(new VasItem(1, 1, 1, 1,1,1),
                new VasItem(1, 1, 1, 1,1,1));
        when(vasItemRepository.findAll()).thenReturn(mockItems);

        // when
        List<VasItem> result = vasItemService.getAllVasItems();

        //then
        assertEquals(mockItems, result);
    }

    @Test
    void it_should_delete_item_when_item_exist_in_cart() {
        // given
        VasItem vasItem = new VasItem(1, 7889, 1, 1,1,1);
        when(vasItemRepository.findItemByItemId(1)).thenReturn(java.util.Optional.of(vasItem));
        when(responseService.createApiResponse(eq(true), anyString()))
                .thenReturn(new ResponseEntity<>(Map.of("success", true, "message", "Item deleted from cart"), HttpStatus.OK));

        // when
        var result = vasItemService.deleteItemFromCart(1);

        //then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(Map.of("success", true, "message", "Item deleted from cart"), result.getBody());
    }

    @Test
    void it_should_not_delete_item_when_item_does_not_exist_in_cart(){

        //given
        when(vasItemRepository.findItemByItemId(1)).thenReturn(java.util.Optional.empty());
        when(responseService.createApiResponse(eq(false), anyString()))
                .thenReturn(new ResponseEntity<>(Map.of("success", false, "message", "Item not found"), HttpStatus.OK));

        // when
        var result = vasItemService.deleteItemFromCart(1);

        //then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(Map.of("success", false, "message", "Item not found"), result.getBody());
    }

    @Test
    void it_should_throw_exception_when_vasItem_not_valid(){
        //Vas item default category id and seller id is 3242 and 5003
        //given
        ReflectionTestUtils.setField(vasItemService, "vasItemDefaultCategoryId", 3242);
        ReflectionTestUtils.setField(vasItemService, "vasItemDefaultSellerId", 5003);
        VasItem vasItem =new VasItem(1, 1, 1, 1,1,1);

        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            vasItemService.isVasItemValid(vasItem);
        });

        //then
        assertEquals("Vas item is not valid", exception.getMessage());

    }
    @Test
    void it_should_not_throw_exception_when_vasItem_is_valid(){
        //Vas item default category id and seller id is 3242 and 5003
        //given
        ReflectionTestUtils.setField(vasItemService, "vasItemDefaultCategoryId", 3242);
        ReflectionTestUtils.setField(vasItemService, "vasItemDefaultSellerId", 5003);
        VasItem vasItem =new VasItem(1, 3242, 5003, 1,1,1);

        //when
        vasItemService.isVasItemValid(vasItem);

        //then
        assertDoesNotThrow(() -> vasItemService.isVasItemValid(vasItem));

    }

    @Test
    void it_should_throw_exception_when_vasItem_limit_is_not_valid(){
        //Vas item limit is 3
        //given
        ReflectionTestUtils.setField(vasItemService, "DEFAULT_VAS_ITEM_LIMIT", 3);
        VasItem vasItem =new VasItem(1, 3242, 5003, 1,4,1);

        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            vasItemService.isVasItemLimitValid(List.of(vasItem));
        });

        //then
        assertEquals("Maximum quantity exceeded for Item ID " + vasItem.getItemId() + " Quantity: " + vasItem.getQuantity(), exception.getMessage());

    }

    @Test
    void it_should_not_throw_exception_when_vasItem_limit_is_valid(){
        //Vas item limit is 3
        //given
        ReflectionTestUtils.setField(vasItemService, "DEFAULT_VAS_ITEM_LIMIT", 3);
        VasItem vasItem =new VasItem(1, 3242, 5003, 1,3,1);

        //when
        vasItemService.isVasItemLimitValid(List.of(vasItem));

        //then
        assertDoesNotThrow(() -> vasItemService.isVasItemLimitValid(List.of(vasItem)));

    }

    @Test
    void it_should_throw_exception_when_default_item_does_not_exist_in_cart(){
        //Valid default item category ids are 1001 and 3004
        //given
        ReflectionTestUtils.setField(vasItemService, "validDefaultItemCategoryForVasItem", "1001,3004");
        VasItem vasItem =new VasItem(1, 3242, 5003, 1,3,1);
        when(itemRepository.findAll()).thenReturn(List.of());

        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            vasItemService.isVasItemItemIdExistInDefaultItem(vasItem);
        });

        //then
        assertEquals("DefaultItem not found in the cart", exception.getMessage());

    }

    @Test
    void it_should_not_throw_exception_when_default_item_exist_in_cart(){

        //Valid default item category ids are 1001 and 3004
        //given
        ReflectionTestUtils.setField(vasItemService, "validDefaultItemCategoryForVasItem", "1001,3004");
        DefaultItem defaultItem =new DefaultItem(1, 1001, 1, 1,1, List.of());
        VasItem vasItem =new VasItem(1, 3242, 5003, 1,3,1);

        when(itemRepository.findAll()).thenReturn(List.of(defaultItem));

        //when
        vasItemService.isVasItemItemIdExistInDefaultItem(vasItem);

        //then
        assertDoesNotThrow(() -> vasItemService.isVasItemItemIdExistInDefaultItem(vasItem));

    }
   @Test
    void it_should_add_vasItem_when_vasItem_does_not_exist_in_cart(){
        //given
        VasItem vasItem =new VasItem(1, 3242, 5003, 1,3,1);
        when(vasItemRepository.findByItemIdAndVasItemId(1, 3242)).thenReturn(null);
        when(vasItemRepository.save(vasItem)).thenReturn(vasItem);
        when(itemRepository.findAll()).thenReturn(List.of());

        //when
        vasItemService.addVasItem(vasItem);

        //then
        verify(vasItemRepository, times(1)).save(vasItem);
    }



}