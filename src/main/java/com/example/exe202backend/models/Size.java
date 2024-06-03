package com.example.exe202backend.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Table
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Size extends BaseModel{
    private String name;

    @OneToMany(mappedBy = "size", cascade = CascadeType.ALL)
    private List<ProductMaterial> productMaterials;
}
