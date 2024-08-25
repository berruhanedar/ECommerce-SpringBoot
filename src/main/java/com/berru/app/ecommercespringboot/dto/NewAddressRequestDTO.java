package com.berru.app.ecommercespringboot.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NewAddressRequestDTO {


    @Pattern(regexp = "[A-Za-z0-9\\s-]{3,}", message = "Not a valid address name")
    private String addressName;

    @Pattern(regexp = "[A-Za-z0-9\\s-]{3,}", message = "Not a valid street")
    private String street;

    @Pattern(regexp = "[A-Za-z0-9\\s-]{3,}", message = "Not a valid building name")
    private String buildingName;

    @NotNull(message = "City name cannot be null")
    @Pattern(regexp = "[A-Za-z\\s]{2,}", message = "Not a valid city name")
    private String city;

    @NotNull(message = "Country name cannot be null")
    private String country;

    @NotNull(message = "Postal code cannot be null")
    @Pattern(regexp = "[0-9]{6}", message = "Postal code not valid. Must be 6 digits")
    private String postalCode;
}
