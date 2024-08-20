package com.berru.app.ecommercespringboot.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AdressDTO {
    private int addressId;
    private String addressName;
    private String street;
    private String buildingName;
    private String city;
    private String country;
    private int postalCode;
}
