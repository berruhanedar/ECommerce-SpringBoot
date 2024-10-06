package com.berru.app.ecommercespringboot.entity;

import com.berru.app.ecommercespringboot.converter.ObjectToStringConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ProductAttributeValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "attribute_id")
    private Attribute attribute;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @NotNull
    @Convert(converter = ObjectToStringConverter.class) // Doğru dönüştürücü kullanıldığından emin olun
    private Object value; // Dinamik değer

    @NotNull
    @Column(nullable = false, columnDefinition = "varchar(255)")
    private String type; // Değerin türü

    public ProductAttributeValue(Attribute attribute, Product product, Object value) {
        this.attribute = attribute;
        this.product = product;
        this.value = value;
        this.type = determineValueType(value); // Girilen değere göre tür belirleniyor
    }

    private String determineValueType(Object value) {
        if (value instanceof String) {
            return "String";
        } else if (value instanceof Integer) {
            return "Integer";
        } else if (value instanceof Double) {
            return "Double";
        } else if (value instanceof List) {
            return "List";
        } else if (value instanceof Date) {
            return "Date"; // Tarih türü
        } else {
            return "Unknown"; // Bilinmeyen tür
        }
    }
}
