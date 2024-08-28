package com.berru.app.ecommercespringboot.service;


import com.berru.app.ecommercespringboot.dto.ShoppingCartDTO;
import com.berru.app.ecommercespringboot.entity.Customer;
import com.berru.app.ecommercespringboot.entity.Product;
import com.berru.app.ecommercespringboot.entity.ShoppingCart;
import com.berru.app.ecommercespringboot.exception.ResourceNotFoundException;
import com.berru.app.ecommercespringboot.mapper.ShoppingCartMapper;
import com.berru.app.ecommercespringboot.repository.CustomerRepository;
import com.berru.app.ecommercespringboot.repository.ProductRepository;
import com.berru.app.ecommercespringboot.repository.ShoppingCartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;


    @Transactional
    public void addToCart(Integer customerId, Integer productId, Integer quantity) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (product.getQuantity() < quantity) {
            throw new IllegalArgumentException("Not enough quantity available");
        }

        ShoppingCart cart = shoppingCartRepository.findById(customerId)
                .orElseGet(() -> new ShoppingCart(customer));

        cart.addItem(product, quantity);

        product.setQuantity(product.getQuantity() - quantity);

        productRepository.save(product);

        shoppingCartRepository.save(cart);
    }


    @Transactional
    public void removeItemFromCart(Integer cartId, Integer productId) {
        ShoppingCart cart = shoppingCartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("ShoppingCart not found with id: " + cartId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        cart.removeItem(product);
        shoppingCartRepository.save(cart);
    }


    @Transactional
    public ShoppingCartDTO getShoppingCartById(Integer id) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ShoppingCart not found with id: " + id));
        return shoppingCartMapper.toDTO(shoppingCart);
    }

    @Transactional
    public void deleteShoppingCart(Integer id) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ShoppingCart not found with id: " + id));
        shoppingCartRepository.delete(shoppingCart);
    }

    @Transactional
    public void checkoutCart(Integer id) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ShoppingCart not found with id: " + id));

        shoppingCart.checkout();
        shoppingCartRepository.save(shoppingCart);
    }
}
