package com.example.exe202backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CartDTO {
    private long id;
    private double total;
    private boolean status;
    private long userId;
}
