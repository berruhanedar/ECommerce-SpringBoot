package com.berru.app.ecommercespringboot.repository;

import com.berru.app.ecommercespringboot.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
    List<Address> findByAddressIdIn(List<Integer> addressIds);
}
