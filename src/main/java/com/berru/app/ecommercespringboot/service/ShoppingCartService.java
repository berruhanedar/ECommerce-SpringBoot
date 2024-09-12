package com.berru.app.ecommercespringboot.service;

import com.berru.app.ecommercespringboot.dto.AddToCartRequestDTO;
import com.berru.app.ecommercespringboot.dto.ShoppingCartDTO;
import com.berru.app.ecommercespringboot.entity.Customer;
import com.berru.app.ecommercespringboot.entity.Product;
import com.berru.app.ecommercespringboot.entity.ShoppingCart;
import com.berru.app.ecommercespringboot.exception.InsufficientQuantityException;
import com.berru.app.ecommercespringboot.exception.ResourceNotFoundException;
import com.berru.app.ecommercespringboot.mapper.ShoppingCartMapper;
import com.berru.app.ecommercespringboot.repository.CustomerRepository;
import com.berru.app.ecommercespringboot.repository.ProductRepository;
import com.berru.app.ecommercespringboot.repository.ShoppingCartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    @Transactional
    public ShoppingCartDTO addToCart(AddToCartRequestDTO addToCartRequestDTO) {
        Customer customer = customerRepository.findById(addToCartRequestDTO.getCustomerId().intValue())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + addToCartRequestDTO.getCustomerId()));

        Product product = productRepository.findById(addToCartRequestDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + addToCartRequestDTO.getProductId()));

        if (product.getQuantity() < addToCartRequestDTO.getQuantity()) {
            throw new InsufficientQuantityException("Not enough quantity available for product ID: " + addToCartRequestDTO.getProductId());
        }

        ShoppingCart shoppingCart = shoppingCartRepository.findByCustomerId(customer.getId())
                .orElseGet(() -> new ShoppingCart(customer));

        shoppingCart.addItem(product, addToCartRequestDTO.getQuantity());

        shoppingCart = shoppingCartRepository.save(shoppingCart);

        return shoppingCartMapper.toDTO(shoppingCart);
    }


    @Transactional
    @CacheEvict(value = "shoppingCarts", key = "#cartId")
    public void removeItemFromCart(Integer cartId, Integer productId) {
        ShoppingCart cart = shoppingCartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("ShoppingCart not found with id: " + cartId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        cart.removeItem(product);
        shoppingCartRepository.save(cart);
    }

    @Transactional
    @Cacheable(value = "shoppingCarts", key = "#customerId", unless = "#result ==null")
    public ShoppingCartDTO getShoppingCartByCustomerId(int customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + customerId));

        ShoppingCart shoppingCart = shoppingCartRepository.findByCustomerIdWithItems(customerId)
                .orElseGet(() -> {
                    ShoppingCart newCart = new ShoppingCart(customer);
                    shoppingCartRepository.save(newCart);
                    return newCart;
                });

        return shoppingCartMapper.toDTO(shoppingCart);
    }

    @Transactional
    @Cacheable(value = "shoppingCarts", key = "#id")
    public void deleteShoppingCart(Integer id) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ShoppingCart not found with id: " + id));
        shoppingCartRepository.delete(shoppingCart);
    }

    @Transactional
    @Cacheable(value = "shoppingCarts", key = "#id")
    public void checkoutCart(Integer id) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ShoppingCart not found with id: " + id));

        shoppingCart.checkout();
        shoppingCartRepository.save(shoppingCart);
    }
}