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
@Entity
public class AddresDto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer addressId;

    @Pattern(regexp = "[A-Za-z0-9\\s-]{3,}", message = "Not a valid street no")
    private String streetNo;

    @Pattern(regexp = "[A-Za-z0-9\\s-]{3,}", message = "Not a valid building name")
    private String buildingName;

    @NotNull
    @Pattern(regexp = "[A-Za-z0-9\\s-]{3,}", message = "Not a valid locality name")
    private String locality;

    @NotNull(message = "City name cannot be null")
    @Pattern(regexp = "[A-Za-z\\s]{2,}", message = "Not a valid city name")
    private String city;

    @NotNull(message = "State name cannot be null")
    private String state;

    @NotNull(message = "Pincode cannot be null")
    @Pattern(regexp = "[0-9]{6}", message = "Pincode not valid. Must be 6 digits")
    private String pincode;


    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    private Customer customer;

}
