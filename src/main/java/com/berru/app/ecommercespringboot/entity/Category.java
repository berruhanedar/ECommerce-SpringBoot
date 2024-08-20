package com.berru.app.ecommercespringboot.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "category")
@Builder
public class Category {
    @Id
    @Column(name = "category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "category_name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category categoryParent;

    @OneToMany(mappedBy = "categoryParent")
    private List<Category> categoryChildren = new ArrayList<>();

    // Constructor for creating a new Category with parent
    @Builder
    public Category(String name, Category categoryParent) {
        this.name = name;
        this.categoryParent = categoryParent;
    }

    // Constructor for updating Category
    @Builder
    public Category(Integer id, String name, Category categoryParent, List<Category> categoryChildren) {
        this.id = id;
        this.name = name;
        this.categoryParent = categoryParent;
        this.categoryChildren = categoryChildren;
    }

    // Method for updating category name
    public void updateCategory(String name) {
        this.name = name;
    }
}
