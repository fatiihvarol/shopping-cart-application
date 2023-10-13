package com.example.trendyolcasestudy.controller.item;

import com.example.trendyolcasestudy.model.item.DefaultItem;
import com.example.trendyolcasestudy.model.item.Item;
import com.example.trendyolcasestudy.service.item.DefaultItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/default-item")
public class DefaultItemController {

    private final DefaultItemService defaultItemService;

    @Autowired
    public DefaultItemController(DefaultItemService defaultItemService) {
        this.defaultItemService = defaultItemService;
    }

    @GetMapping
    public List<Item> getAllItemsFromCart() {
        return defaultItemService.getAllItems();
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> addItemToCart(@RequestBody DefaultItem newItem) {
       return defaultItemService.addItemToCart(newItem);

    }

    @DeleteMapping(path = "{itemId}")
    public ResponseEntity<Map<String, Object>> deleteItemFromCart(@PathVariable int itemId) {
        return defaultItemService.deleteItemFromCart(itemId);
    }
}
