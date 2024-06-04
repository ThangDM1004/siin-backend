package com.example.exe202backend.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SizeDTO {
    private Long id;
    private String name;
    private double price;
}
