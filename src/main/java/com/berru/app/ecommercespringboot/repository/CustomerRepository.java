package com.berru.app.ecommercespringboot.repository;

import com.berru.app.ecommercespringboot.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    List<Customer> findByAddressIdIn(List<Integer> addressId, Pageable pageable);
}
