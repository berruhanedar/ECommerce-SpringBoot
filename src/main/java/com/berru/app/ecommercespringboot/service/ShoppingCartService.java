package com.berru.app.ecommercespringboot.service;


import com.berru.app.ecommercespringboot.dto.ShoppingCartDTO;
import com.berru.app.ecommercespringboot.entity.Customer;
import com.berru.app.ecommercespringboot.entity.Product;
import com.berru.app.ecommercespringboot.entity.ShoppingCart;
import com.berru.app.ecommercespringboot.enums.ShoppingCartStatus;
import com.berru.app.ecommercespringboot.exception.ResourceNotFoundException;
import com.berru.app.ecommercespringboot.mapper.ShoppingCartMapper;
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

    public void addToCart(Integer customerId, Integer productId, Integer quantity) {
        ShoppingCart cart = shoppingCartRepository.findByCustomerId(customerId)
                .orElseGet(() -> createNewCartForCustomer(customerId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        cart.addItem(product, quantity);
        shoppingCartRepository.save(cart);
    }

    private ShoppingCart createNewCartForCustomer(Integer customerId) {
        ShoppingCart cart = new ShoppingCart();
        Customer customer = new Customer();
        customer.setId(customerId);
        cart.setCustomer(customer);
        return shoppingCartRepository.save(cart);
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

        shoppingCart.setStatus(ShoppingCartStatus.CHECKED_OUT);
        shoppingCart.checkout();
        shoppingCartRepository.save(shoppingCart);
    }
}
