package com.berru.app.ecommercespringboot.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NewAddressRequestDTO {
    private Integer addressId;

    @Pattern(regexp = "[A-Za-z0-9\\s-]{3,}", message = "Not a valid address name")
    private String addressName;

    @Pattern(regexp = "[A-Za-z0-9\\s-]{3,}", message = "Not a valid street no")
    private String streetNo;

    @Pattern(regexp = "[A-Za-z0-9\\s-]{3,}", message = "Not a valid building name")
    private String buildingName;

    @NotNull(message = "City name cannot be null")
    @Pattern(regexp = "[A-Za-z\\s]{2,}", message = "Not a valid city name")
    private String city;

    @NotNull(message = "Country name cannot be null")
    private String country;

    @NotNull(message = "Postalcode cannot be null")
    @Pattern(regexp = "[0-9]{6}", message = "Postalcode not valid. Must be 6 digits")
    private String postalcode;
}
