package com.berru.app.ecommercespringboot.controller;


import com.berru.app.ecommercespringboot.dto.AddToCartRequestDTO;
import com.berru.app.ecommercespringboot.dto.ShoppingCartDTO;
import com.berru.app.ecommercespringboot.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;


@RequiredArgsConstructor
@RestController
@RequestMapping("/shopping-carts")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;


    @GetMapping("/{id}")
    public ResponseEntity<ShoppingCartDTO> getShoppingCartById(@PathVariable Integer id) {
        ShoppingCartDTO shoppingCart = shoppingCartService.getShoppingCartById(id);
        return ResponseEntity.ok(shoppingCart);
    }


    @PostMapping("/add")
    public ResponseEntity<Void> addToCart(@RequestBody AddToCartRequestDTO request) {
        shoppingCartService.addToCart(request.getCustomerId(), request.getProductId(), request.getQuantity());
        return ResponseEntity.ok().build();
    }


    @PostMapping("/{id}/checkout")
    public ResponseEntity<Void> checkoutCart(@PathVariable Integer id) {
        shoppingCartService.checkoutCart(id);
        return ResponseEntity.ok().build();
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShoppingCart(@PathVariable Integer id) {
        shoppingCartService.deleteShoppingCart(id);
        return ResponseEntity.noContent().build();
    }
}
