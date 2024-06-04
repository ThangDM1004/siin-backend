package com.example.exe202backend.controllers;

import com.example.exe202backend.dto.*;
import com.example.exe202backend.response.ResponseObject;
import com.example.exe202backend.services.OrderDetailService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/v1/order-detail")
@CrossOrigin(origins = "*")
public class OrderDetailController {
    @Autowired
    private OrderDetailService orderDetailService;

    @GetMapping(value = "/get-all/{currentPage}")
    public ResponseEntity<ResponseObject> getAll(@PathVariable int currentPage
            , @RequestParam(defaultValue = "5") int pageSize
            , @RequestParam(defaultValue = "id") String field) {
        if(currentPage < 1 || pageSize < 1 || currentPage > pageSize){
            return ResponseEntity.ok(new ResponseObject("get success", orderDetailService.get()));
        }
        Page<OrderDetailDTO> orderDetailDTOS = orderDetailService.getAll(currentPage, pageSize, field);
        var pageList = PageList.<OrderDetailDTO>builder()
                .totalPage(orderDetailDTOS.getTotalPages())
                .currentPage(currentPage)
                .listResult(orderDetailDTOS.getContent())
                .build();
        return ResponseEntity.ok(new ResponseObject("get success", pageList));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getById(@PathVariable long id) {
        return orderDetailService.getById(id);
    }
    @PostMapping
    public ResponseEntity<ResponseObject> create(@RequestParam(required = false) Long userId,
                                                 @RequestBody OrderDetailRequestDTO_2 orderDetailRequestDTO_2) {
        return orderDetailService.create(userId,orderDetailRequestDTO_2.getCartItems(),
                orderDetailRequestDTO_2.getOrderDetailRequestDTO());
    }
    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> update(@PathVariable long id,@RequestBody OrderDetailDTO orderDetailDTO) {
        return orderDetailService.update(id,orderDetailDTO);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> delete(@PathVariable long id) {
        return orderDetailService.delete(id);
    }
}
