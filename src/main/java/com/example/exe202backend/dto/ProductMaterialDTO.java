package com.example.exe202backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductMaterialDTO {
    private String colorName;
    private String size;
    private int quantity;
    private String image;
    private double price;
    private boolean status;
}
