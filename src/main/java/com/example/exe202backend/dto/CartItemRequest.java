package com.example.exe202backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CartItemRequest {
    private Long colorId;
    private Long sizeId;
    private Long accessoryId;
    private int quantity;
}
