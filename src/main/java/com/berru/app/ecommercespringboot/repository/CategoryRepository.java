package com.berru.app.ecommercespringboot.repository;

import com.berru.app.ecommercespringboot.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.children WHERE c.parentCategory.id = :parentCategoryId")
    List<Category> findByParentCategoryId(@Param("parentCategoryId") Integer parentCategoryId);

}
