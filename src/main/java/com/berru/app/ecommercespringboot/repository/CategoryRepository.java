package com.berru.app.ecommercespringboot.repository;

import com.berru.app.ecommercespringboot.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    @Query(value = "WITH RECURSIVE category_tree AS ("
            + "SELECT c.category_id, c.category_name, c.parent_id "
            + "FROM category c "
            + "WHERE c.category_id = :categoryId "
            + "UNION ALL "
            + "SELECT c.category_id, c.category_name, c.parent_id "
            + "FROM category c "
            + "INNER JOIN category_tree ct ON c.parent_id = ct.category_id"
            + ") "
            + "SELECT * FROM category_tree", nativeQuery = true)
    List<Category> findCategoryTreeById(@Param("categoryId") Integer categoryId);
}
