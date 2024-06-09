package com.example.exe202backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AccessoryDTO {
    private long id;
    private String name;
    private double price;
    private String image;
    private boolean status;
}
