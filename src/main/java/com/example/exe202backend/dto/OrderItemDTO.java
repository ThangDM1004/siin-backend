package com.example.exe202backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OrderItemDTO {
    private int quantity;
    private double price;
    private long productId;
    private long orderDetailId;
    private boolean status;
}
