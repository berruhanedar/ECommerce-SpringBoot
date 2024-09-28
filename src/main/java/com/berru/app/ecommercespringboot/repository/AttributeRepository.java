package com.berru.app.ecommercespringboot.repository;

import com.berru.app.ecommercespringboot.entity.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttributeRepository extends JpaRepository<Attribute, Integer> {
}
