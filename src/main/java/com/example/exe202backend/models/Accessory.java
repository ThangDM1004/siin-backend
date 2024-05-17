package com.example.exe202backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
@Builder
public class Accessory extends BaseModel{
    private String name;
    private int quantity;
    private String image;
    private double price;

    @OneToMany(mappedBy = "accessory", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Product> products;
}
