package com.example.trendyolcasestudy.controller.item;

import com.example.trendyolcasestudy.model.item.DigitalItem;
import com.example.trendyolcasestudy.service.item.DigitalItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/digital-item")
public class DigitalItemController {
    private final DigitalItemService digitalItemService;


    @Autowired
    public DigitalItemController(DigitalItemService digitalItemService) {
        this.digitalItemService = digitalItemService;
    }

    @GetMapping
    public List<DigitalItem> getAllItemsFromCart() {
        return digitalItemService.getAllIDigitalItems();
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> addItemToCart(@RequestBody DigitalItem newItem) {
        return digitalItemService.addItemToCart(newItem);

    }

    @DeleteMapping(path = "{itemId}")
    public ResponseEntity<Map<String, Object>> deleteItemFromCart(@PathVariable int itemId) {
        return digitalItemService.deleteItemFromCart(itemId);
    }
}
