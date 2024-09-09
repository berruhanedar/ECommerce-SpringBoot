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
        // Müşteriyi bul
        Customer customer = customerRepository.findById(addToCartRequestDTO.getCustomerId().intValue())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + addToCartRequestDTO.getCustomerId()));

        // Ürünü bul
        Product product = productRepository.findById(addToCartRequestDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + addToCartRequestDTO.getProductId()));

        // İstenen miktarın stokta olup olmadığını kontrol et
        if (product.getQuantity() < addToCartRequestDTO.getQuantity()) {
            throw new InsufficientQuantityException("Not enough quantity available for product ID: " + addToCartRequestDTO.getProductId());
        }

        // Müşterinin alışveriş sepetini bul veya yeni bir tane oluştur
        ShoppingCart shoppingCart = shoppingCartRepository.findByCustomerId(customer.getId())
                .orElseGet(() -> {
                    // Yeni alışveriş sepeti oluştur ve müşteriyi ilişkilendir
                    ShoppingCart newCart = new ShoppingCart(customer);
                    newCart.setCustomer(customer); // Müşteriyle ilişkilendir
                    return newCart;
                });

        // Ürünü sepete ekle
        shoppingCart.addItem(product, addToCartRequestDTO.getQuantity());

        // Sepeti kaydet
        shoppingCart = shoppingCartRepository.save(shoppingCart);

        // Sepeti DTO'ya dönüştür ve geri döndür
        return shoppingCartMapper.toDTO(shoppingCart);
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
    public ShoppingCartDTO getShoppingCartByCustomerId(int customerId) {
        // Müşteriyi bul
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + customerId));

        // Alışveriş sepetini bul ya da yeni bir tane oluştur
        ShoppingCart shoppingCart = shoppingCartRepository.findByCustomerId(customerId)
                .orElseGet(() -> {
                    ShoppingCart newCart = new ShoppingCart(customer);
                    shoppingCartRepository.save(newCart); // Yeni sepete kaydet
                    return newCart;
                });

        // Sepeti DTO'ya dönüştür ve geri döndür
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
