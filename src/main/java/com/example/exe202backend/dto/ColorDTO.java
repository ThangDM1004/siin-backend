package com.example.exe202backend.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ColorDTO {
    private Long id;
    private String name;
    private double price;
}
