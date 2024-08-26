package com.berru.app.ecommercespringboot.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateShoppingCartRequestDTO {
    @NotNull(message = "Total price cannot be null")
    private BigDecimal totalPrice;
}
