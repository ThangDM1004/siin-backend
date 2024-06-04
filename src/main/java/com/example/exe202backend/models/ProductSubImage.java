package com.example.exe202backend.models;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
@SuperBuilder
public class ProductSubImage extends BaseModel{
    private String url;

    @ManyToOne
    @JoinColumn(name = "product_material_id")
    private ProductMaterial productMaterial;
}
