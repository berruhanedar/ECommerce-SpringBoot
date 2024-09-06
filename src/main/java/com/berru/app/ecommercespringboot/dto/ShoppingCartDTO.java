package com.berru.app.ecommercespringboot.dto;

import com.berru.app.ecommercespringboot.enums.ShoppingCartStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ShoppingCartDTO {
    private Integer id;
    private BigDecimal totalPrice;
    private CustomerDTO customer; // CustomerDTO kullanılabilir, müşteri detayları için
    private List<ShoppingCartItemDTO> items; // Alışveriş sepeti kalemleri
    private AddressDTO address; // AddressDTO kullanılabilir
    private ShoppingCartStatus status; // Sepet durumu
}