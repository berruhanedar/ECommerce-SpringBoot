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
        // Müşteriyi bul veya hata fırlat
        Customer customer = customerRepository.findById(addToCartRequestDTO.getCustomerId().intValue())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + addToCartRequestDTO.getCustomerId()));

        // Ürünü bul veya hata fırlat
        Product product = productRepository.findById(addToCartRequestDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + addToCartRequestDTO.getProductId()));

        // Ürün stok kontrolü
        if (product.getQuantity() < addToCartRequestDTO.getQuantity()) {
            throw new InsufficientQuantityException("Not enough quantity available for product ID: " + addToCartRequestDTO.getProductId());
        }

        // Alışveriş sepetini bul veya müşteri için yeni bir sepet oluştur
        ShoppingCart shoppingCart = shoppingCartRepository.findByCustomerId(customer.getId())
                .orElseGet(() -> new ShoppingCart(customer));

        // Sepete ürün ekle
        shoppingCart.addItem(product, addToCartRequestDTO.getQuantity());

        // Sepeti kaydet
        shoppingCart = shoppingCartRepository.save(shoppingCart);

        // DTO'ya dönüştürüp geri döndür
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
