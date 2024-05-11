package com.example.exe202backend.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
@Builder
public class OrderDetail extends BaseModel{
    private double total;
    private String orderStatus;
    private String phone;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserModel userModel;

    @OneToOne(mappedBy = "orderDetail")
    private Payment payment;
}
