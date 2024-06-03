package com.example.exe202backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductDTO {
    private long id;
    private String name;
    private String coverImage;
    private double price;
    private boolean status;
    private long categoryId;
}
