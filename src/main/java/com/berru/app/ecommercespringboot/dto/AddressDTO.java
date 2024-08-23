package com.berru.app.ecommercespringboot.dto;


import com.berru.app.ecommercespringboot.entity.Customer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
public class AddressDTO {

    private Integer addressId;
    private String addressName;
    private String street;
    private String buildingName;
    private String city;
    private String country;
    private Integer postalCode;
}
