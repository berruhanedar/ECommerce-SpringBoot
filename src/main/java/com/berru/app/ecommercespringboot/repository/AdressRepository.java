package com.berru.app.ecommercespringboot.repository;

import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdressRepository extends JpaRepository<RabbitConnectionDetails.Address, Integer> {
}
