package com.berru.app.ecommercespringboot.repository;

import com.berru.app.ecommercespringboot.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    Page<Order> findAllByCustomerIdOrderByOrderDateDesc(Integer customerId, Pageable pageable);
}