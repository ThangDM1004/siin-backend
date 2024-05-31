package com.example.exe202backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private String nameCustomer;
    private String phone;
    private String province;
    private String district;
    private String ward;
    private String address;
    private String email;
    private String note;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = true)
    private UserModel userModel;

    @OneToOne(mappedBy = "orderDetail")
    @JsonIgnore
    private Payment payment;

    @OneToMany(mappedBy = "orderDetail", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;
}
