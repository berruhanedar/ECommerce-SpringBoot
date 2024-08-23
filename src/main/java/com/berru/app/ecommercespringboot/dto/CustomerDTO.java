package com.berru.app.ecommercespringboot.dto;


import com.berru.app.ecommercespringboot.enums.Status;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

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
    private Status status;

}
