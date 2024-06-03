package com.example.exe202backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductMaterialDTO {
    private Long id;
    private Long colorId;
    private Long sizeId;
    private Long productId;
    private int quantity;
    private String image;
    private double price;
    private boolean status;
}
