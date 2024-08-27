package com.berru.app.ecommercespringboot.dto;

import lombok.Data;

@Data
public class AddressDTO {

    private Integer id;
    private String addressName;
    private String street;
    private String buildingName;
    private String city;
    private String country;
    private String postalCode;
}
