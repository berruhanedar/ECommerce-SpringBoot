package com.berru.app.ecommercespringboot.controller;

import com.berru.app.ecommercespringboot.dto.AddToCartRequestDTO;
import com.berru.app.ecommercespringboot.dto.ShoppingCartDTO;
import com.berru.app.ecommercespringboot.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/shopping-carts")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;


    @GetMapping("/{customerId}")
    public ResponseEntity<ShoppingCartDTO> getShoppingCartByCustomerId(@PathVariable Integer customerId) {
        ShoppingCartDTO shoppingCartDTO = shoppingCartService.getShoppingCartByCustomerId(customerId);
        return ResponseEntity.ok(shoppingCartDTO);
    }


    @PostMapping("/add")
    public ResponseEntity<ShoppingCartDTO> addToCart(@RequestBody AddToCartRequestDTO addToCartRequestDTO) {
        ShoppingCartDTO shoppingCartDTO = shoppingCartService.addToCart(addToCartRequestDTO);
        return new ResponseEntity<>(shoppingCartDTO, HttpStatus.OK);
    }


    @PostMapping("/{id}/checkout")
    public ResponseEntity<Void> checkoutCart(@PathVariable Integer id) {
        shoppingCartService.checkoutCart(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<Void> removeItemFromCart(@PathVariable Integer cartId, @PathVariable Integer productId) {
        shoppingCartService.removeItemFromCart(cartId, productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShoppingCart(@PathVariable Integer id) {
        shoppingCartService.deleteShoppingCart(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public List<ShoppingCartDTO> searchShoppingCartsByRsql(@RequestParam String query) {
        return shoppingCartService.searchShoppingCartsByRsql(query);
    }
}
