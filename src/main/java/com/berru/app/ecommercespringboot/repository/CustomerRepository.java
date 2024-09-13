package com.berru.app.ecommercespringboot.repository;

import com.berru.app.ecommercespringboot.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> , JpaSpecificationExecutor<Customer> {
    Optional<Customer> findById(Integer customerId);
}
