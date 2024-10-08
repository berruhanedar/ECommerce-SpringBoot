package com.berru.app.ecommercespringboot.dto;


import com.berru.app.ecommercespringboot.enums.CustomerStatus;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.math.BigDecimal;

@Data
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
    private String mobileNumber;

    @NotNull
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    private String password;

    @NotNull(message = "Balance cannot be null")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal balance;

    @NotNull(message = "Status cannot be null")
    private CustomerStatus status;

    @NotNull
    private Integer addressId;
}
