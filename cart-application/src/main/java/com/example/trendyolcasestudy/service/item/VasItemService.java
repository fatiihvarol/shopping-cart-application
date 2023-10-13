package com.example.trendyolcasestudy.service.item;

import com.example.trendyolcasestudy.model.item.DefaultItem;
import com.example.trendyolcasestudy.model.item.Item;
import com.example.trendyolcasestudy.model.item.VasItem;
import com.example.trendyolcasestudy.repository.ItemRepository;
import com.example.trendyolcasestudy.repository.VasItemRepository;
import com.example.trendyolcasestudy.response.ResponseService;
import com.example.trendyolcasestudy.service.cart.CartValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class VasItemService {

    private final int  DEFAULT_VAS_ITEM_LIMIT = 3;

    private final VasItemRepository vasItemRepository;

    private  final ItemRepository itemRepository;

    @Value("${vas.item.default.category.id}")
    private int vasItemDefaultCategoryId;

    @Value("${vas.item.default.seller.id}")
    private int vasItemDefaultSellerId;

    private final CartValidation<Item> cartValidation;


    private final ResponseService responseService;

    @Value("${vas.item.can.be.added.to}")
    private String validDefaultItemCategoryForVasItem;

    @Autowired
    public VasItemService(VasItemRepository vasItemRepository, ItemRepository itemRepository, CartValidation <Item> cartValidation,ResponseService responseService) {
        this.vasItemRepository = vasItemRepository;
        this.itemRepository = itemRepository;
        this.cartValidation = cartValidation;
        this.responseService = responseService;
    }

    public List<VasItem> getAllVasItems() {
        return vasItemRepository.findAll();
    }
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public ResponseEntity<Map<String, Object>> addItemToCart(VasItem newItem) {
        List<VasItem> items = getAllVasItems();     items.add(newItem);
        List<Item> allItems=getAllItems();          allItems.add(newItem);

        cartValidation.isCartTotalAmountValid(allItems);
        cartValidation.isCartTotalItemCountValid(allItems);
        cartValidation.isItemQuantityValid(allItems);
        cartValidation.isCardValidForNoDigitalItems(allItems);

        isVasItemLimitValid(items);
        isVasItemValid(newItem);
        isVasItemItemIdExistInDefaultItem(newItem);

        addVasItem(newItem);
        return responseService.createApiResponse(true, "Item added to cart");
 }

    public ResponseEntity<Map<String, Object>> deleteItemFromCart(int itemId) {
        Optional<VasItem> optionalItem = vasItemRepository.findItemByItemId(itemId);
        if (optionalItem.isPresent()) {
            itemRepository.delete(optionalItem.get());
            return responseService.createApiResponse(true, "Item deleted from cart");
        }
        return responseService.createApiResponse(false, "Item not found");
    }

     void isVasItemValid(VasItem vasItem) {
        boolean isValid= vasItem.getCategoryId()==vasItemDefaultCategoryId && vasItem.getSellerId()==vasItemDefaultSellerId;

        if(!isValid){
            throw new IllegalArgumentException("Vas item is not valid");
        }
    }

     void isVasItemLimitValid(List<VasItem> vasItems) {
        vasItems.stream()
                .collect(Collectors
                .groupingBy(VasItem::getItemId, Collectors
                .summingLong(VasItem::getQuantity)))
                .forEach((itemId, totalQuantity) -> {
                    if (totalQuantity > DEFAULT_VAS_ITEM_LIMIT) {
                        throw new IllegalArgumentException("Maximum quantity exceeded for Item ID " + itemId + " Quantity: " + totalQuantity);
                    }
                });
}

     void isVasItemItemIdExistInDefaultItem(VasItem vasItem) {

        List<Item> items= itemRepository.findAll()
                .stream()
                .filter(item -> (item instanceof DefaultItem))
                .filter(item -> item.getItemId()==vasItem.getItemId())
                .toList();

        if (items.isEmpty()) {
        throw new IllegalArgumentException("DefaultItem not found in the cart");
     }

    Item item = items.get(0);

    String[] validCategoryIds = validDefaultItemCategoryForVasItem.split(",");

    if (!Arrays.asList(validCategoryIds).contains(String.valueOf(item.getCategoryId()))) {
        throw new IllegalArgumentException("Default Item category is not valid for Vas Item");
    }
    }

     void addVasItem(VasItem vasItem) {

        VasItem existVasItem = vasItemRepository
                .findByItemIdAndVasItemId(vasItem.getItemId(), vasItem.getVasItemId());

        if (existVasItem != null) {
            int newQuantity = existVasItem.getQuantity() + vasItem.getQuantity();
            existVasItem.setQuantity(newQuantity);
            vasItemRepository.save(existVasItem);

        }else {
            vasItemRepository.save(vasItem);
        }
        Optional<Item> items = itemRepository.findAll()
                .stream()
                .filter(item -> (item instanceof DefaultItem))
                .filter(item -> item.getItemId()==vasItem.getItemId())
                .findFirst();
        if (items.isPresent()) {
            DefaultItem defaultItem = (DefaultItem) items.get();
            List<VasItem> defaultItemsVasItem=defaultItem.getVasItems();
            defaultItemsVasItem.add(vasItem);
            defaultItem.setVasItems(defaultItemsVasItem);
            itemRepository.save(defaultItem);
        }

    }



}
