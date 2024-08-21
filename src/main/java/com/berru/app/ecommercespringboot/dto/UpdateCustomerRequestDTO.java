package com.berru.app.ecommercespringboot.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UpdateCustomerRequestDTO {
    private int customerId;
    private int addressId;
    private String firstName;
    private String lastName;
    private String email;
    private Integer mobileNumber;
    private String password;
    private BigDecimal balance;
}