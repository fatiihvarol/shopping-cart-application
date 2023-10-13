package com.example.trendyolcasestudy.controller.item;

import com.example.trendyolcasestudy.model.item.VasItem;
import com.example.trendyolcasestudy.service.item.VasItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/vas-item")
public class VasItemController {
     private final VasItemService vasItemService;
        @Autowired
        public VasItemController(VasItemService vasItemService) {
            this.vasItemService = vasItemService;
        }

    @GetMapping
    public List<VasItem> getAllItemsFromCart() {
        return vasItemService.getAllVasItems();
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> addItemToCart(@RequestBody VasItem newItem) {
        return vasItemService.addItemToCart(newItem);

    }

    @DeleteMapping(path = "{itemId}")
    public ResponseEntity<Map<String, Object>> deleteItemFromCart(@PathVariable int itemId) {
        return vasItemService.deleteItemFromCart(itemId);
    }
}
