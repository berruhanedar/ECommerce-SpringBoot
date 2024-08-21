package com.berru.app.ecommercespringboot.dto;


import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CustomerDTO {
    private Integer id;

    @NotNull
    @Size(min = 2, max = 50, message = "First name size be between 2-50")
    private String firstName;

    @NotNull
    @Size(min = 2, max = 50, message = "Last name size be between 2-50")
    private String lastName;

    @NotNull
    @Size(min = 5, max = 50)
    @Email(message = "Email should be valid")
    private String email;

    @NotNull
    @Size(min = 10, max = 10)
    private Integer mobileNumber;

    @NotNull
    @Size(min = 8, max = 20)
    private String password;

    @NotNull
    @Digits(integer = 10, fraction = 2)
    private BigDecimal balance;

    @NotNull
    private Integer addressId;

}
