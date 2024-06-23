package com.example.exe202backend.mapper;

import com.example.exe202backend.dto.CartItemDTO;
import com.example.exe202backend.dto.CartItemResponseDTO;
import com.example.exe202backend.dto.CartItemResponseDTO_2;
import com.example.exe202backend.models.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
    @Mapping(target = "status", source = "status")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "productMaterialId", source = "productMaterial.id")
    @Mapping(target = "cartId", source = "cart.id")
    CartItemDTO toDto(CartItem cartItem);

    @Mapping(target = "status", source = "status")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "productMaterial", ignore = true)
    @Mapping(target = "cart", ignore = true)
    CartItem toEntity(CartItemDTO cartItemDTO);

    @Mapping(target = "status", source = "status")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "productMaterial", ignore = true)
    @Mapping(target = "cart", ignore = true)
    void updateCartItemFromDto(CartItemDTO cartItemDTO, @MappingTarget CartItem cartItem);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "sizeName", source = "productMaterial.size.name")
    @Mapping(target = "colorName", source = "productMaterial.color.name")
    @Mapping(target = "accessoryName", source = "productMaterial.accessory.name")
    @Mapping(target = "productId", source = "productMaterial.product.id")
    @Mapping(target = "cartId", source = "cart.id")
    @Mapping(target = "accessoryId", source = "productMaterial.accessory.id")
    @Mapping(target = "productMaterialId", source = "productMaterial.id")
    @Mapping(target = "status", source = "status")
    CartItemResponseDTO toResponseDto(CartItem cartItem);

    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "sizeName", source = "productMaterial.size.name")
    @Mapping(target = "colorName", source = "productMaterial.color.name")
    @Mapping(target = "accessoryName", source = "productMaterial.accessory.name")
    @Mapping(target = "productId", source = "productMaterial.product.id")
    @Mapping(target = "accessoryId", source = "productMaterial.accessory.id")
    CartItemResponseDTO_2 toResponseDto_2(CartItem cartItem);
}
