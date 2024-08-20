package com.berru.app.ecommercespringboot.repository;

import com.berru.app.ecommercespringboot.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
  }
