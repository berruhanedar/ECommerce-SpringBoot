package com.berru.app.ecommercespringboot.controller;


import com.berru.app.ecommercespringboot.dto.NewShoppingCartRequestDTO;
import com.berru.app.ecommercespringboot.dto.ShoppingCartDTO;
import com.berru.app.ecommercespringboot.dto.UpdateShoppingCartRequestDTO;
import com.berru.app.ecommercespringboot.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/shopping-carts")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @PostMapping
    public ResponseEntity<ShoppingCartDTO> createShoppingCart(@RequestBody @Validated NewShoppingCartRequestDTO newShoppingCartRequestDTO) {
        ShoppingCartDTO createdShoppingCart = shoppingCartService.createShoppingCart(newShoppingCartRequestDTO);
        return new ResponseEntity<>(createdShoppingCart, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShoppingCartDTO> getShoppingCartById(@PathVariable Integer id) {
        ShoppingCartDTO shoppingCart = shoppingCartService.getShoppingCartById(id);
        return ResponseEntity.ok(shoppingCart);
    }

    @GetMapping
    public ResponseEntity<List<ShoppingCartDTO>> getAllShoppingCarts() {
        List<ShoppingCartDTO> shoppingCarts = shoppingCartService.getAllShoppingCarts();
        return ResponseEntity.ok(shoppingCarts);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShoppingCartDTO> updateShoppingCart(@PathVariable Integer id, @RequestBody @Validated
            UpdateShoppingCartRequestDTO updateShoppingCartRequestDTO) {
        ShoppingCartDTO updatedShoppingCart = shoppingCartService.updateShoppingCart(id, updateShoppingCartRequestDTO);
        return ResponseEntity.ok(updatedShoppingCart);
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addToCart(@RequestParam Integer customerId, @RequestParam Integer productId, @RequestParam int quantity) {
        shoppingCartService.addToCart(customerId, productId, quantity);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShoppingCart(@PathVariable Integer id) {
        shoppingCartService.deleteShoppingCart(id);
        return ResponseEntity.noContent().build();
    }
}
