package com.berru.app.ecommercespringboot.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Category {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToOne
    @JoinColumn(name = "category_parent_id")
    private Category categoryParent;

    public void compare(Category category)
    {
        if (category.getCategoryParent()!= null) this.categoryParent= category.getCategoryParent();
        if (category.getName() != null ) this.name = category.getName();
    }


}