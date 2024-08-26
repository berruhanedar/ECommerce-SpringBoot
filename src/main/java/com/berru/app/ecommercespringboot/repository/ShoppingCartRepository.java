package com.berru.app.ecommercespringboot.repository;

import com.berru.app.ecommercespringboot.entity.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Integer> {
}
