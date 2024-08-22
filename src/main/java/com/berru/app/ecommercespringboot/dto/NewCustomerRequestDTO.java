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
public class NewCustomerRequestDTO {

    @NotNull
    @Size(min = 2, max = 50, message = "First name size be between 2-50")
    private String firstName;

    @NotNull
    @Size(min = 2, max = 50, message = "Last name size be between 2-50")
    private String lastName;

    @NotNull(message = "Please enter the email")
    @Size(min = 5, max = 50, message = "Email must be between 5 and 50 characters")
    @Email(message = "Email should be valid")
    private String email;

    @NotNull
    @Size(min = 10, max = 10, message = "Mobile number must be 10 digits")
    private Integer mobileNumber;

    @NotNull
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    private String password;

    @NotNull(message = "Balance cannot be null")
    @Digits(integer = 10, fraction = 2, message = "Balance must be a numeric value with up to 10 digits in the integer part and 2 digits in the fractional part")
    private BigDecimal balance;

    @NotNull
    private Integer addressId;

}
