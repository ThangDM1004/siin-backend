package com.example.exe202backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PaymentDTO {
    private long id;
    private boolean status;
    private String typePayment;
    private double total;
    private long orderDetailId;
}
