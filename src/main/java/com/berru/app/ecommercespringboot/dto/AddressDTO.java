package com.berru.app.ecommercespringboot.dto;


import com.berru.app.ecommercespringboot.entity.Customer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AddressDTO {

    private Integer addressId;

    @Pattern(regexp = "[A-Za-z0-9\\s-]{3,}")
    private String addressName;

    @Pattern(regexp = "[A-Za-z0-9\\s-]{3,}")
    private String streetNo;

    @Pattern(regexp = "[A-Za-z0-9\\s-]{3,}")
    private String buildingName;

    @NotNull(message = "City name cannot be null")
    @Pattern(regexp = "[A-Za-z\\s]{2,}")
    private String city;

    @NotNull(message = "Country name cannot be null")
    @Pattern(regexp = "[A-Za-z\\s]{2,}")
    private String country;

    @NotNull(message = "Postalcode cannot be null")
    @Pattern(regexp = "[0-9]{6}")
    private String postalCode;


    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    private Customer customer;

}
