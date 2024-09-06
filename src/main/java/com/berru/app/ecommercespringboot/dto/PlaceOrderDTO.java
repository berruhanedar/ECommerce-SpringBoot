package com.berru.app.ecommercespringboot.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaceOrderDTO {

    @NotNull(message = "Customer ID cannot be null")
    @Min(value = 1, message = "Customer ID must be at least 1")
    private Integer customerId;

    @NotNull(message = "Address ID cannot be null")
    @Min(value = 1, message = "Address ID must be at least 1")
    private Integer addressId;

}
