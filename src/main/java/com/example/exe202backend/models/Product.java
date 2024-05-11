package com.example.exe202backend.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
@Builder
public class Product extends BaseModel{
    private String name;
    private String coverImage;
    private double price;
    private String status;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private ProductCategory category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductSubImage> productSubImages;

    @OneToOne(mappedBy = "product")
    private Accessory accessory;

    @OneToOne(mappedBy = "product")
    private ProductMaterial productMaterial;

    @OneToOne(mappedBy = "product")
    private CartItem cartItem;

    @OneToOne(mappedBy = "product")
    private OrderItem orderItem;
}
