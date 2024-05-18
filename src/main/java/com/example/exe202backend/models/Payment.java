package com.example.exe202backend.models;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
public class Payment extends BaseModel{
    private String typePayment;
    private double total;

    @OneToOne
    @JoinColumn(name = "order_detail_id")
    private OrderDetail orderDetail;
}
