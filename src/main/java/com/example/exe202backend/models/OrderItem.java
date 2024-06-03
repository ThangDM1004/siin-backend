package com.example.exe202backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
@SuperBuilder
public class OrderItem extends BaseModel{
    private int quantity;
    private double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_material_id")
    @JsonIgnore
    private ProductMaterial productMaterial;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_detail_id")
    @JsonIgnore
    private OrderDetail orderDetail;
}
