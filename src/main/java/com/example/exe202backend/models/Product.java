package com.example.exe202backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private ProductCategory category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ProductSubImage> productSubImages;

    @ManyToOne
    @JoinColumn(name = "accessory_id")
    private Accessory accessory;

    @ManyToOne
    @JoinColumn(name = "productMaterial_id")
    private ProductMaterial productMaterial;

    @OneToOne(mappedBy = "product")
    @JsonIgnore
    private CartItem cartItem;

    @OneToOne(mappedBy = "product")
    @JsonIgnore
    private OrderItem orderItem;
}
