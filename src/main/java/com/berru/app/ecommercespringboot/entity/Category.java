package com.berru.app.ecommercespringboot.entity;


import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity
@Table(name = "category")
@Builder
public class Category {
    @Id
    @Column(name = "category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Column(name = "category_name")
    private String categoryName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category categoryParent;

    @OneToMany(mappedBy = "categoryParent")
    private List<Category> categoryChildren = new ArrayList<>();

    @Builder(builderMethodName = "create")
    public Category(String categoryName, Category categoryParent) {
        this.categoryName = categoryName;
        this.categoryParent = categoryParent;
    }

    @Builder
    public Category(Long categoryId, String categoryName, Category categoryParent,
                    List<Category> categoryChildren) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryParent = categoryParent;
        this.categoryChildren = categoryChildren;
    }

    public void updateCategory(String categoryName) {
        this.categoryName = categoryName;
    }
}
