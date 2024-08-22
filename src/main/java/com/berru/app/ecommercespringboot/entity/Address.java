package com.berru.app.ecommercespringboot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * entity ve tablo anotasyonunu kontrol et
 */

@Getter
@Setter
@Entity
@Table(name = "addresses")
public class Address {

    /**
     * nullable = false, length = 255 gerek olmaması lazım
     * address_id yerine id kullan
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Integer addressId;

    @Column(name = "address_name", nullable = false, length = 255)
    private String addressName;

    @Column(name = "street", nullable = false, length = 255)
    private String street;

    @Column(name = "building_name", length = 255)
    private String buildingName;

    @Column(name = "city", nullable = false, length = 255)
    private String city;

    @Column(name = "country", nullable = false, length = 255)
    private String country;

    @Column(name = "postal_code", nullable = false)
    private Integer postalCode;


}
