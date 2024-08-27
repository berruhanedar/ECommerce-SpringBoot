package com.berru.app.ecommercespringboot.entity;

import com.berru.app.ecommercespringboot.enums.ShoppingCartStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Column;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;


import lombok.Data;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "shopping_cart_id")
    private Integer id;

    @Column(name = "total price")
    private BigDecimal totalPrice;

    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "shoppingCart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShoppingCartItem> items = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @Enumerated(EnumType.STRING)
    private ShoppingCartStatus status;

    public void addItem(Product product, int quantity) {
        ShoppingCartItem item = new ShoppingCartItem();
        item.setProduct(product);
        item.setQuantity(quantity);
        item.setPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
        item.setShoppingCart(this);
        items.add(item);
        recalculateTotalPrice();
    }

    public void recalculateTotalPrice() {
        totalPrice = items.stream()
                .map(item -> item.getPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    public void checkout() {
        BigDecimal calculatedTotalPrice = items.stream()
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.totalPrice = calculatedTotalPrice;

        if (address != null) {
            System.out.println("Shipping Address:");
            System.out.println("Street: " + address.getStreet());
            System.out.println("Building Name: " + address.getBuildingName());
            System.out.println("City: " + address.getCity());
            System.out.println("Country: " + address.getCountry());
            System.out.println("Postal Code: " + address.getPostalCode());
        } else {
            System.out.println("No address information available.");
        }

        this.status = ShoppingCartStatus.CHECKED_OUT;
    }
}
