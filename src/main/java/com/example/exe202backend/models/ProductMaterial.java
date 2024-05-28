package com.example.exe202backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_material", uniqueConstraints = {@UniqueConstraint(columnNames = {"colorName", "size"})})
@SuperBuilder
public class ProductMaterial extends BaseModel{
    private String colorName;
    private String size;
    private int quantity;
    private String image;
    private double price;

    @OneToMany(mappedBy = "productMaterial", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Product> products;
}
