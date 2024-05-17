package com.example.exe202backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AccessoryDTO {
    private String name;
    private int quantity;
    private String image;
    private double price;
}
