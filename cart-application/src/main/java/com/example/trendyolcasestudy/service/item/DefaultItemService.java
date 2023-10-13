package com.example.trendyolcasestudy.service.item;

import com.example.trendyolcasestudy.model.item.DefaultItem;
import com.example.trendyolcasestudy.model.item.Item;
import com.example.trendyolcasestudy.repository.ItemRepository;
import com.example.trendyolcasestudy.response.ResponseService;
import com.example.trendyolcasestudy.service.cart.CartValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;



@Service
public class DefaultItemService {
    private final  ItemRepository itemRepository;


    private  ResponseService responseService;
    private final CartValidation<Item> cartValidation;

    @Autowired
    public DefaultItemService(ItemRepository itemRepository, CartValidation<Item> cartValidation, ResponseService responseService) {
        this.itemRepository = itemRepository;
        this.cartValidation = cartValidation;
        this.responseService = responseService;
    }


    public List<Item> getAllItems() {
       return itemRepository.findAll();
    }


    public ResponseEntity<Map<String, Object>> addItemToCart(DefaultItem newItem) {

        List<Item> items=getAllItems();   items.add(newItem);

        cartValidation.isCartItemsUnique(items);
        cartValidation.isCartTotalAmountValid(items);
        cartValidation.isCartTotalItemCountValid(items);
        cartValidation.isItemQuantityValid(items);
        cartValidation.isCardValidForNoDigitalItems(items);

        return saveItem(newItem);

    }

    public ResponseEntity<Map<String, Object>> deleteItemFromCart(int itemId) {

        List<DefaultItem> optionalItem = itemRepository.findItemByItemIdforDelete(itemId);
        if (!optionalItem.isEmpty()) {
            itemRepository.deleteAll(optionalItem);
            return responseService.createApiResponse(true, "Item deleted from cart");
        }
        return responseService.createApiResponse(false, "Item not found");
    }

     ResponseEntity<Map<String, Object>> saveItem(DefaultItem item) {
        List<Item> items = getAllItems()
                .stream()
                .filter(i -> i instanceof DefaultItem)
                .filter(i -> i.getItemId() == item.getItemId())
                .toList();

        if (!items.isEmpty()) {
            Item itemToUpdate = items.get(0);
            itemToUpdate.setQuantity(item.getQuantity()+item.getQuantity());
            itemRepository.save(itemToUpdate);
        } else {
            itemRepository.save(item);
        }
        return responseService.createApiResponse(true, "Item added to cart");
    }
}
