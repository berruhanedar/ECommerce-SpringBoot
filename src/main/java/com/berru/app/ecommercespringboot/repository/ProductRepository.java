package com.berru.app.ecommercespringboot.repository;

import com.berru.app.ecommercespringboot.entity.Product.Status;
import com.berru.app.ecommercespringboot.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByCategoryIdIn(List<Integer> categoryIds);

    Page<Product> findByStatusIn(List<Status> statuses, Pageable pageable);
}