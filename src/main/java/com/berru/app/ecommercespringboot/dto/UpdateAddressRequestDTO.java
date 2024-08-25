package com.berru.app.ecommercespringboot.dto;


import com.berru.app.ecommercespringboot.entity.Customer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ManyToOne;
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
public class UpdateAddressRequestDTO {

        @Pattern(regexp = "[A-Za-z0-9\\s-]{3,}")
        private String addressName;

        @Pattern(regexp = "[A-Za-z0-9\\s-]{3,}")
        private String street;

        @Pattern(regexp = "[A-Za-z0-9\\s-]{3,}")
        private String buildingName;

        @NotNull(message = "City name cannot be null")
        @Pattern(regexp = "[A-Za-z\\s]{2,}")
        private String city;

        @NotNull(message = "Country name cannot be null")
        @Pattern(regexp = "[A-Za-z\\s]{2,}")
        private String country;

        @NotNull(message = "Postal code cannot be null")
        @Pattern(regexp = "[0-9]{6}")
        private String postalCode;


        @ManyToOne(cascade = CascadeType.ALL)
        @JsonIgnore
        private Customer customer;
}
