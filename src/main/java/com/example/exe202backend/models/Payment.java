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
public class Payment extends BaseModel{
    private String typePayment;
    private double total;
    private String status;

    @OneToOne
    @JoinColumn(name = "order_detail_id")
    private OrderDetail orderDetail;
}
