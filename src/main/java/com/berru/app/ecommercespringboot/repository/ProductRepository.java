package com.berru.app.ecommercespringboot.repository;

import com.berru.app.ecommercespringboot.entity.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product> {
    List<Product> findByCategoryIdIn(List<Integer> categoryIds);

    @Query("SELECT p.quantity FROM Product p WHERE p.id = :productId")
    Optional<Integer> findStockByProductId(@Param("productId") Integer productId);
}