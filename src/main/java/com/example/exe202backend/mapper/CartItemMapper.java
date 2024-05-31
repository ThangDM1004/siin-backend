package com.example.exe202backend.mapper;

import com.example.exe202backend.dto.CartItemDTO;
import com.example.exe202backend.models.Cart;
import com.example.exe202backend.models.CartItem;
import com.example.exe202backend.models.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
    @Mapping(target = "status", source = "status")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "cartId", source = "cart.id")
    CartItemDTO toDto(CartItem cartItem);

    @Mapping(target = "status", source = "status")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "cart", ignore = true)
    CartItem toEntity(CartItemDTO cartItemDTO);

    @Mapping(target = "status", source = "status")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "cart", ignore = true)
    void updateCartItemFromDto(CartItemDTO cartItemDTO, @MappingTarget CartItem cartItem);
}
