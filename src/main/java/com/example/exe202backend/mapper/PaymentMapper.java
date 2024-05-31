package com.example.exe202backend.mapper;

import com.example.exe202backend.dto.PaymentDTO;
import com.example.exe202backend.models.OrderDetail;
import com.example.exe202backend.models.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    @Mapping(target = "status", source = "status")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "orderDetailId", source = "orderDetail.id")
    PaymentDTO toDto(Payment payment);
    @Mapping(target = "status", source = "status")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "orderDetail", ignore = true)
    Payment toEntity(PaymentDTO paymentDTO);
    @Mapping(target = "status", source = "status")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "orderDetail", ignore = true)
    void updatePaymentFromDto(PaymentDTO paymentDTO, @MappingTarget Payment payment);
}
