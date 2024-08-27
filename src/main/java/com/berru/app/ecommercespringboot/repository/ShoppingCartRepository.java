package com.berru.app.ecommercespringboot.repository;

import com.berru.app.ecommercespringboot.entity.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Integer> {
    Optional<ShoppingCart> findByCustomerId(Integer customerId);
}
