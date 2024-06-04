package com.example.exe202backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailRequestDTO_2 {
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    List<CartItemResponseDTO_2> cartItems;
    OrderDetailRequestDTO orderDetailRequestDTO;
}
