package com.example.exe202backend.services;

import com.example.exe202backend.dto.OrderItemDTO;
import com.example.exe202backend.mapper.OrderItemMapper;
import com.example.exe202backend.models.OrderItem;
import com.example.exe202backend.repositories.OrderItemRepository;
import com.example.exe202backend.response.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderItemService {
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private OrderItemMapper orderItemMapper;

    public ResponseEntity<ResponseObject> create(OrderItemDTO orderItemDTO) {
        OrderItem orderItem = orderItemMapper.toEntity(orderItemDTO);
        orderItemRepository.save(orderItem);
        return ResponseEntity.ok(new ResponseObject("create success",orderItemDTO));
    }

    public Page<OrderItemDTO> getAll(int currentPage, int pageSize, String field){
        Page<OrderItem> orderItems = orderItemRepository.findAll(
                PageRequest.of(currentPage-1,pageSize, Sort.by(Sort.Direction.ASC,field)));
        return orderItems.map(orderItemMapper::toDto);
    }

    public ResponseEntity<ResponseObject> getById(long id){
        OrderItem orderItem = orderItemRepository.findById(id).orElseThrow(()->
                new RuntimeException("Order Item not found"));
        return ResponseEntity.ok(new ResponseObject("get success",orderItemMapper.toDto(orderItem)));
    }
    public ResponseEntity<ResponseObject> delete(long id){
        Optional<OrderItem> orderItem = orderItemRepository.findById(id);
        if(orderItem.isPresent()){
            orderItem.get().setStatus(false);
            orderItemRepository.save(orderItem.get());
            return ResponseEntity.ok(new ResponseObject("delete success",orderItemMapper.toDto(orderItem.get())));
        }
        return ResponseEntity.badRequest().body(new ResponseObject("Order Item not found",null));
    }

    public ResponseEntity<ResponseObject> update(Long id,OrderItemDTO orderItemDTO){
        OrderItem orderItem = orderItemRepository.findById(id).orElseThrow(()->
                new RuntimeException("Order Item not found"));
        orderItemMapper.updateOrderItemFromDto(orderItemDTO,orderItem);
        orderItemRepository.save(orderItem);
        return ResponseEntity.ok(new ResponseObject("update success",orderItemDTO));
    }
}
