package com.example.exe202backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    private Long id;
    private String name;
    private String coverImage;
    private double price;
    private Boolean status;
    private long categoryId;
}
