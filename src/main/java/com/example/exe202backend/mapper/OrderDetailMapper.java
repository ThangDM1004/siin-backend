package com.example.exe202backend.mapper;

import com.example.exe202backend.dto.OrderDetailDTO;
import com.example.exe202backend.dto.OrderDetailRequestDTO;
import com.example.exe202backend.models.OrderDetail;
import com.example.exe202backend.models.Payment;
import com.example.exe202backend.models.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface OrderDetailMapper {
    @Mapping(target = "status", source = "status")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "userId", source = "userModel.id")
    OrderDetailDTO toDto(OrderDetail orderDetail);

    @Mapping(target = "status", source = "status")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "userModel", ignore = true)
    OrderDetail toEntity(OrderDetailDTO orderDetailDTO);

    @Mapping(target = "userModel", ignore = true)
    OrderDetail toEntity(OrderDetailRequestDTO orderDetailDTO);

    @Mapping(target = "status", source = "status")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "userModel", ignore = true)
    void updateOrderDetailFromDto(OrderDetailDTO orderDetailDTO, @MappingTarget OrderDetail orderDetail);
}
