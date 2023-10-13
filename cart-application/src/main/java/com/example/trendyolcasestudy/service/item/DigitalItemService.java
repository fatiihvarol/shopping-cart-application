package com.example.trendyolcasestudy.service.item;


import com.example.trendyolcasestudy.model.item.DigitalItem;
import com.example.trendyolcasestudy.model.item.Item;
import com.example.trendyolcasestudy.repository.DigitalItemRepository;
import com.example.trendyolcasestudy.repository.ItemRepository;
import com.example.trendyolcasestudy.response.ResponseService;
import com.example.trendyolcasestudy.service.cart.CartValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
public class DigitalItemService {
   private final DigitalItemRepository  digitalItemRepository;

   private final ItemRepository itemRepository;
   private final CartValidation<DigitalItem> cardValidation;
   private final ResponseService responseService;

   @Value("${digital.item.default.category.id}")
   private  int digitalItemDefaultCategoryId ;

   @Autowired
    public DigitalItemService(DigitalItemRepository digitalItemRepository, ItemRepository itemRepository, CartValidation<DigitalItem> cardValidation, ResponseService responseService) {
        this.digitalItemRepository = digitalItemRepository;
       this.itemRepository = itemRepository;
       this.cardValidation = cardValidation;
       this.responseService = responseService;
   }

    public List<DigitalItem> getAllIDigitalItems() {
        return digitalItemRepository.findAll();
    }
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public ResponseEntity<Map<String, Object>> addItemToCart(DigitalItem newItem) {
        List<DigitalItem> items = getAllIDigitalItems();  items.add(newItem);
        isDigitalItemCategoryIdValid(newItem);
        isCartHasOnlyDigitalItems();

        cardValidation.isCartTotalAmountValid(items);
        cardValidation.isCartTotalItemCountValid(items);
        cardValidation.isItemQuantityValidForDigitalItems(items);



        return saveItem(newItem);
    }

    public ResponseEntity<Map<String, Object>> deleteItemFromCart(int itemId) {
        Optional<DigitalItem> optionalItem = digitalItemRepository.findItemByItemId(itemId);
        if (optionalItem.isPresent()) {
            digitalItemRepository.delete(optionalItem.get());
            return responseService.createApiResponse(true, "Item deleted from cart");
        }
        return responseService.createApiResponse(false, "Item not found");
    }

     void isDigitalItemCategoryIdValid(DigitalItem digitalItem) {
       int categoryId = digitalItem.getCategoryId();
        if (categoryId != digitalItemDefaultCategoryId) {
            throw new IllegalArgumentException("Digital item category id must be " + digitalItemDefaultCategoryId);
        }
    }

     void isCartHasOnlyDigitalItems() {
        List<Item> allCartItems = itemRepository.findAll();

        List<Item> nonDigitalItems = allCartItems.stream()
                .filter(item -> !(item instanceof DigitalItem))
                .toList();

        if (!nonDigitalItems.isEmpty()) {
            throw new IllegalArgumentException("Cart can only have digital items");
        }

    }
    public ResponseEntity<Map<String, Object>> saveItem(DigitalItem item) {
        List<Item> items = getAllItems()
                .stream()
                .filter(i -> i instanceof DigitalItem)
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
