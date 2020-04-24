package com.in4mo.shopping.basket.controller;

import com.in4mo.shopping.basket.model.PurchasedItems;
import com.in4mo.shopping.basket.service.BasketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BasketController {

    private final BasketService basketService;

    @Autowired
    public BasketController(BasketService basketService) {
        this.basketService = basketService;
    }

    @GetMapping("/items")
    public ResponseEntity<String> getAllItemsInShop() {
        return ResponseEntity.ok().body(basketService.getItemsWithPrice());
    }

    @PutMapping("/receipt")
    public ResponseEntity<String> getReceiptAfterPriceCalculation(@RequestBody final PurchasedItems payload) {
        return ResponseEntity.ok().body(basketService.getReceiptAfterCalculation(payload));
    }
}
