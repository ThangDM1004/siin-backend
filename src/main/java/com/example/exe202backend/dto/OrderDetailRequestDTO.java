package com.example.exe202backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OrderDetailRequestDTO {
    private String nameCustomer;
    private String phone;
    private String province;
    private String district;
    private String ward;
    private String address;
    private String email;
    private String note;
}
