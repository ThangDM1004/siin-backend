package com.example.exe202backend.mapper;

import com.example.exe202backend.dto.OrderItemDTO;
import com.example.exe202backend.models.OrderDetail;
import com.example.exe202backend.models.OrderItem;
import com.example.exe202backend.models.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    @Mapping(target = "status", source = "status")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "productMaterialId", source = "productMaterial.id")
    @Mapping(target = "orderDetailId", source = "orderDetail.id")
    OrderItemDTO toDto(OrderItem orderItem);

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
