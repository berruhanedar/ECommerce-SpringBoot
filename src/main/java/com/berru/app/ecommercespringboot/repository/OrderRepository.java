package com.berru.app.ecommercespringboot.repository;

import com.berru.app.ecommercespringboot.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findAllByCustomerIdOrderByOrderDateDesc(Integer customerId);

}
