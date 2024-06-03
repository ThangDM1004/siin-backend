package com.example.exe202backend.mapper;

import com.example.exe202backend.dto.OrderItemDTO;
import com.example.exe202backend.dto.OrderItemResponseDTO;
import com.example.exe202backend.models.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    @Mapping(target = "status", source = "status")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "productMaterialId", source = "productMaterial.id")
    @Mapping(target = "orderDetailId", source = "orderDetail.id")
    OrderItemDTO toDto(OrderItem orderItem);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "sizeName", source = "productMaterial.size.name")
    @Mapping(target = "colorName", source = "productMaterial.color.name")
    @Mapping(target = "accessoryName", source = "productMaterial.accessory.name")
    @Mapping(target = "productId", source = "productMaterial.product.id")
    @Mapping(target = "orderDetailId", source = "orderDetail.id")
    @Mapping(target = "status", source = "status")
    OrderItemResponseDTO toOrderItemResponseDTO(OrderItem orderItem);

    @Mapping(target = "status", source = "status")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "productMaterial", ignore = true)
    @Mapping(target = "orderDetail", ignore = true)
    OrderItem toEntity(OrderItemDTO orderItemDTO);

    @Mapping(target = "status", source = "status")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "productMaterial", ignore = true)
    @Mapping(target = "orderDetail", ignore = true)
    void updateOrderItemFromDto(OrderItemDTO orderItemDTO, @MappingTarget OrderItem orderItem);
}
