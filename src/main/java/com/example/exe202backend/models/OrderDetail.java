package com.example.exe202backend.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
@SuperBuilder
public class OrderDetail extends BaseModel{
    private double total;
    private String orderStatus;
    private String phone;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserModel userModel;

    @OneToOne(mappedBy = "orderDetail")
    private Payment payment;

    @OneToMany(mappedBy = "orderDetail", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;
}
