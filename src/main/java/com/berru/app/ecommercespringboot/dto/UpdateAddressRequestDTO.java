package com.berru.app.ecommercespringboot.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateAddressRequestDTO {
        private int addressId;
        private String addressName;
        private String streetNo;
        private String buildingName;
        private String city;
        private String country;
        private int postalCode;
}
