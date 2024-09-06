package com.berru.app.ecommercespringboot.dto;

import com.berru.app.ecommercespringboot.entity.Product;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ShoppingCartItemDTO {
    private Integer id;
    private Product product; // Ürün detayları için ProductDTO kullanılabilir
    private Integer quantity;
    private BigDecimal price;
}
