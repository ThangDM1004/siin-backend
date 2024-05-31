package com.example.exe202backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OrderDetailDTO {
    private long id;
    private double total;
    private String orderStatus;
    private boolean status;
    private String nameCustomer;
    private String phone;
    private String province;
    private String district;
    private String ward;
    private String address;
    private String email;
    private String note;
    private long userId;
}
