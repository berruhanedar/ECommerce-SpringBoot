package com.berru.app.ecommercespringboot.service;


import com.berru.app.ecommercespringboot.dto.NewShoppingCartRequestDTO;
import com.berru.app.ecommercespringboot.dto.ShoppingCartDTO;
import com.berru.app.ecommercespringboot.dto.UpdateShoppingCartRequestDTO;
import com.berru.app.ecommercespringboot.entity.Customer;
import com.berru.app.ecommercespringboot.entity.Product;
import com.berru.app.ecommercespringboot.entity.ShoppingCart;
import com.berru.app.ecommercespringboot.exception.ResourceNotFoundException;
import com.berru.app.ecommercespringboot.mapper.ShoppingCartMapper;
import com.berru.app.ecommercespringboot.repository.ProductRepository;
import com.berru.app.ecommercespringboot.repository.ShoppingCartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final ProductRepository productRepository;

    public void addToCart(Integer customerId, Integer productId, int quantity) {
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
    public ShoppingCartDTO createShoppingCart(NewShoppingCartRequestDTO newShoppingCartRequestDTO) {
        ShoppingCart shoppingCart = shoppingCartMapper.toEntity(newShoppingCartRequestDTO);
        ShoppingCart savedShoppingCart = shoppingCartRepository.save(shoppingCart);
        return shoppingCartMapper.toDTO(savedShoppingCart);
    }

    @Transactional
    public ShoppingCartDTO getShoppingCartById(Integer id) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ShoppingCart not found with id: " + id));
        return shoppingCartMapper.toDTO(shoppingCart);
    }

    @Transactional
    public List<ShoppingCartDTO> getAllShoppingCarts() {
        return shoppingCartRepository.findAll().stream()
                .map(shoppingCartMapper::toDTO)
                .toList();
    }

    @Transactional
    public ShoppingCartDTO updateShoppingCart(Integer id, UpdateShoppingCartRequestDTO updateShoppingCartRequestDTO) {
        ShoppingCart existingShoppingCart = shoppingCartRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ShoppingCart not found with id: " + id));

        shoppingCartMapper.updateShoppingCartFromDTO(updateShoppingCartRequestDTO, existingShoppingCart);

        ShoppingCart updatedShoppingCart = shoppingCartRepository.save(existingShoppingCart);
        return shoppingCartMapper.toDTO(updatedShoppingCart);
    }

    @Transactional
    public void deleteShoppingCart(Integer id) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ShoppingCart not found with id: " + id));
        shoppingCartRepository.delete(shoppingCart);
    }
}
