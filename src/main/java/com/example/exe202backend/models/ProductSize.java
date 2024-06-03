package com.example.exe202backend.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
@SuperBuilder
public class ProductSize extends BaseModel{
    private String name;
    private int quantity;
    @OneToMany(mappedBy = "productSize", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductProductSize> productProductSizes;
}
