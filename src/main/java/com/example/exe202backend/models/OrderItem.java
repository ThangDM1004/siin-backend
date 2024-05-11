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
public class OrderItem extends BaseModel{
    private int quantity;
    private double price;

    @OneToOne
    @JoinColumn(name = "cart_item_id")
    private CartItem cartItem;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
