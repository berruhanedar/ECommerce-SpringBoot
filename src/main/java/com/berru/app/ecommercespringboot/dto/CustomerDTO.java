package com.berru.app.ecommercespringboot.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class CustomerDTO {
    private int customerId;
    private int adressId;
    private String firstName;
    private String lastName;
    private String email;
    private Integer mobilenumber;
    private String password;
    private BigDecimal balance;

}
