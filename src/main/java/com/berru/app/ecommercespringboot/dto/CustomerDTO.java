package com.berru.app.ecommercespringboot.dto;


import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Data
public class CustomerDTO {

    private int customerId;
    private int addressId;
    private String firstName;
    private String lastName;
    private String email;
    private Integer mobileNumber;
    private String password;
    private BigDecimal balance;

}
