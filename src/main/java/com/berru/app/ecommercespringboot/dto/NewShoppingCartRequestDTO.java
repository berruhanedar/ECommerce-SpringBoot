package com.berru.app.ecommercespringboot.dto;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class NewShoppingCartRequestDTO {

        @NotNull(message = "Customer id cannot be null")
        private Integer customerId;

        @NotNull(message = "Total price cannot be null")
        @DecimalMin(value = "0.00")
        private BigDecimal totalPrice;
    }
