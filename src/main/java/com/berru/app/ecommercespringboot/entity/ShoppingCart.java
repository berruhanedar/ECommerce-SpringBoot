package com.berru.app.ecommercespringboot.entity;

import com.berru.app.ecommercespringboot.enums.ShoppingCartStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ToString.Exclude
    @OneToMany(mappedBy = "shoppingCart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ShoppingCartItem> items = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private ShoppingCartStatus status;

    public ShoppingCart() {
        this.totalPrice = BigDecimal.ZERO;
        this.status = ShoppingCartStatus.PENDING;
        this.customer = new Customer();
    }

    public ShoppingCart(Customer customer) {
        this.customer = customer != null ? customer : new Customer();
        this.totalPrice = BigDecimal.ZERO;
        this.status = ShoppingCartStatus.PENDING;
    }

    public void addItem(Product product, int quantity) {
        ShoppingCartItem item = new ShoppingCartItem();
        item.setProduct(product);
        item.setQuantity(quantity);
        item.setPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
        item.setShoppingCart(this);
        items.add(item);
        recalculateTotalPrice();
    }

    public void removeItem(Product product) {
        items.removeIf(item -> item.getProduct().equals(product));
        updateTotalPrice();
    }

    private void updateTotalPrice() {
        this.totalPrice = items.stream()
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void recalculateTotalPrice() {
        totalPrice = items.stream()
                .map(ShoppingCartItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void checkout() {
        BigDecimal calculatedTotalPrice = items.stream()
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.totalPrice = calculatedTotalPrice;
        this.status = ShoppingCartStatus.CHECKED_OUT;
    }
}
