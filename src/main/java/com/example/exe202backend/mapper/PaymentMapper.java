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
    @Mapping(target = "orderDetail", source = "orderDetailId", qualifiedByName = "orderDetailFromId")
    Payment toEntity(PaymentDTO paymentDTO);
    @Mapping(target = "status", source = "status")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "orderDetail", source = "orderDetailId", qualifiedByName = "orderDetailFromId")
    void updatePaymentFromDto(PaymentDTO paymentDTO, @MappingTarget Payment payment);

    @Named("orderDetailFromId")
    default OrderDetail orderDetailFromId(long id) {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setId(id);
        return orderDetail;
    }
}
