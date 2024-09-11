package com.berru.app.ecommercespringboot.entity;

import com.berru.app.ecommercespringboot.enums.CustomerStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "id")
    private Integer id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @Column(name = "password")
    private String password;

    @Column(name = "balance")
    private BigDecimal balance;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private List<Address> addresses = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "shopping_cart_id")
    private ShoppingCart shoppingCart;

    @JsonManagedReference
    @JsonIgnore
    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private CustomerStatus status;

    public Customer(Integer id) {
        this.id = id;
    }
}

