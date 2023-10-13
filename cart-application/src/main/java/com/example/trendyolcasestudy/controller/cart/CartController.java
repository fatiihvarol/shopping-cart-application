package com.example.trendyolcasestudy.controller.cart;

import com.example.trendyolcasestudy.model.cart.Cart;
import com.example.trendyolcasestudy.service.cart.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/v1/cart")
    public Cart displayCart() {
        return cartService.displayCart();
    }
    @DeleteMapping("/v1/cart")
    public ResponseEntity<Map<String, Object>> resetCart() {
        return cartService.resetCart();
    }
}
