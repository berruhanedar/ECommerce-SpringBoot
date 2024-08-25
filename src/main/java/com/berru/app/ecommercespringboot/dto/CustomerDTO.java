package com.berru.app.ecommercespringboot.dto;


import com.berru.app.ecommercespringboot.enums.CustomerStatus;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CustomerDTO {

    private Integer customerId;
    private Integer addressId;
    private String firstName;
    private String lastName;
    private String email;
    private Integer mobileNumber;
    private String password;
    private BigDecimal balance;
    private CustomerStatus status;
}
