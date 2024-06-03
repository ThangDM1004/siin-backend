package com.example.exe202backend.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponseDTO {
    private long id;
    private int quantity;
    private String sizeName;
    private String colorName;
    private String accessoryName;
    private Long productId;
    private long cartId;
    private boolean status;
}
