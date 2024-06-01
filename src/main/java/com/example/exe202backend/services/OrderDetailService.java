package com.example.exe202backend.services;

import com.example.exe202backend.dto.OrderDetailDTO;
import com.example.exe202backend.mapper.OrderDetailMapper;
import com.example.exe202backend.models.OrderDetail;
import com.example.exe202backend.repositories.OrderDetailRepository;
import com.example.exe202backend.repositories.UserRepository;
import com.example.exe202backend.response.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderDetailService {
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private UserRepository userRepository;

    public List<OrderDetailDTO> get(){
        return orderDetailRepository.findAll().stream().map(orderDetailMapper::toDto).collect(Collectors.toList());
    }

    public ResponseEntity<ResponseObject> create(OrderDetailDTO orderDetailDTO) {
        OrderDetail orderDetail = orderDetailMapper.toEntity(orderDetailDTO);
        if(orderDetailDTO.getUserId() == 0){
            orderDetail.setUserModel(null);
        }
        orderDetail.setUserModel(userRepository.findById(orderDetailDTO.getUserId()).orElse(null));
        orderDetailRepository.save(orderDetail);
        return ResponseEntity.ok(new ResponseObject("create success",orderDetailDTO));
    }
    public Page<OrderDetailDTO> getAll(int currentPage, int pageSize, String field){
        Page<OrderDetail> orderDetails = orderDetailRepository.findAll(
                PageRequest.of(currentPage-1,pageSize, Sort.by(Sort.Direction.ASC,field)));
        return orderDetails.map(orderDetailMapper::toDto);
    }
    public ResponseEntity<ResponseObject> getById(long id){
        OrderDetail orderDetail = orderDetailRepository.findById(id).orElseThrow(()->
                new RuntimeException("Order detail not found"));
        return ResponseEntity.ok(new ResponseObject("get success",orderDetailMapper.toDto(orderDetail)));
    }
    public ResponseEntity<ResponseObject> delete(long id){
        Optional<OrderDetail> orderDetail = orderDetailRepository.findById(id);
        if(orderDetail.isPresent()){
            orderDetail.get().setStatus(false);
            orderDetailRepository.save(orderDetail.get());
            return ResponseEntity.ok(new ResponseObject("delete success",orderDetailMapper.toDto(orderDetail.get())));
        }
        return ResponseEntity.badRequest().body(new ResponseObject("Order detail not found",null));
    }

    public ResponseEntity<ResponseObject> update(Long id,OrderDetailDTO orderDetailDTO){
        OrderDetail existingOrderDetail = orderDetailRepository.findById(id).orElseThrow(()
                -> new RuntimeException("Order detail not found"));

        orderDetailDTO.setOrderStatus(orderDetailDTO.getOrderStatus() != null ? orderDetailDTO.getOrderStatus() : existingOrderDetail.getOrderStatus());
        orderDetailDTO.setUserId(orderDetailDTO.getUserId() != 0 ? orderDetailDTO.getUserId() : existingOrderDetail.getUserModel().getId());
        orderDetailDTO.setEmail(orderDetailDTO.getEmail() != null ? orderDetailDTO.getEmail() : existingOrderDetail.getEmail());
        orderDetailDTO.setPhone(orderDetailDTO.getPhone() != null ? orderDetailDTO.getPhone() : existingOrderDetail.getPhone());
        orderDetailDTO.setAddress(orderDetailDTO.getAddress() != null ? orderDetailDTO.getAddress() : existingOrderDetail.getAddress());
        orderDetailDTO.setDistrict(orderDetailDTO.getDistrict() != null ? orderDetailDTO.getDistrict() : existingOrderDetail.getDistrict());
        orderDetailDTO.setProvince(orderDetailDTO.getProvince() != null ? orderDetailDTO.getProvince() : existingOrderDetail.getProvince());
        orderDetailDTO.setWard(orderDetailDTO.getWard() != null ? orderDetailDTO.getWard() : existingOrderDetail.getWard());
        orderDetailDTO.setNameCustomer(orderDetailDTO.getNameCustomer() != null ? orderDetailDTO.getNameCustomer() : existingOrderDetail.getNameCustomer());
        orderDetailDTO.setNote(orderDetailDTO.getNote() != null ? orderDetailDTO.getNote() : existingOrderDetail.getNote());
        orderDetailDTO.setTotal(orderDetailDTO.getTotal() != 0 ? orderDetailDTO.getTotal() : existingOrderDetail.getTotal());

        orderDetailDTO.setStatus(existingOrderDetail.getStatus());
        orderDetailMapper.updateOrderDetailFromDto(orderDetailDTO,existingOrderDetail);
        existingOrderDetail.setUserModel(userRepository.findById(orderDetailDTO.getUserId()).orElse(null));
        orderDetailRepository.save(existingOrderDetail);
        return ResponseEntity.ok(new ResponseObject("update success",orderDetailDTO));
    }
}
