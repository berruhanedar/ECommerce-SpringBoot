package com.berru.app.ecommercespringboot.repository;

import com.berru.app.ecommercespringboot.entity.Customer;
import com.berru.app.ecommercespringboot.entity.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Integer> {
    @Query("SELECT sc FROM ShoppingCart sc JOIN sc.customer c WHERE c.id = :customerId")
    Optional<ShoppingCart> findByCustomerId(@Param("customerId") Integer customerId);

    @Query("SELECT sc FROM ShoppingCart sc LEFT JOIN FETCH sc.items WHERE sc.customer.id = :customerId")
    Optional<ShoppingCart> findByCustomerIdWithItems(@Param("customerId") Integer customerId);

        ShoppingCart findByCustomer(Customer customer);
}
