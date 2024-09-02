package com.berru.app.ecommercespringboot.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaceOrderDTO {

    @NotNull(message = "Order ID cannot be null")
    private Integer id;

    @NotNull(message = "Customer ID cannot be null")
    @Min(value = 1, message = "Customer ID must be at least 1")
    private Integer customerId;

    @NotNull(message = "Address ID cannot be null")
    @Min(value = 1, message = "Address ID must be at least 1")
    private Integer addressId;

    @NotNull(message = "Total amount cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Total amount must be greater than 0")
    private BigDecimal totalAmount;
}
