package com.example.exe202backend.models;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
@Builder
public class ProductMaterial extends BaseModel{
    private String colorName;
    private String size;
    private int quantity;
    private String image;
    private double price;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
