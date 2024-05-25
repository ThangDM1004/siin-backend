package com.example.exe202backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CartItemDTO {
    private long id;
    private int quantity;
    private long productId;
    private long cartId;
    private boolean status;
}
