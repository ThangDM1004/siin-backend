package com.example.exe202backend.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class PaymentLinkRequest {
    private String productName;
    private String description;
    private String returnUrl;
    private int price;
    private String cancelUrl;
}
