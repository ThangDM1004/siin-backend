package com.example.exe202backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateProductMaterialDTO {
    private Long id;
    private Long productId;
    private Long accessoryId;
    private int quantity;
    private double price;
    private boolean status;
}
