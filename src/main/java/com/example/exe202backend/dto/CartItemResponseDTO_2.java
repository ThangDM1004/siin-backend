package com.example.exe202backend.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponseDTO_2 {
    private int quantity;
    private String sizeName;
    private String colorName;
    private String accessoryName;
    private Long productId;
    private Long productMaterialId;
}
