package com.example.exe202backend.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponseDTO {
    private Long id;
    private int quantity;
    private String sizeName;
    private String colorName;
    private String accessoryName;
    private Long productId;
    private Long accessoryId;
    private Long cartId;
    private Long productMaterialId;
    private boolean status;
}
