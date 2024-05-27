package com.example.exe202backend.controllers;

import com.example.exe202backend.dto.OrderDetailDTO;
import com.example.exe202backend.dto.PageList;
import com.example.exe202backend.response.ResponseObject;
import com.example.exe202backend.services.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/order-detail")
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
    public ResponseEntity<ResponseObject> create(@RequestBody OrderDetailDTO orderDetailDTO) {
        return orderDetailService.create(orderDetailDTO);
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
