package com.example.exe202backend.controllers;

import com.example.exe202backend.dto.OrderItemDTO;
import com.example.exe202backend.dto.OrderItemResponseDTO;
import com.example.exe202backend.dto.PageList;
import com.example.exe202backend.response.ResponseObject;
import com.example.exe202backend.services.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/order-item")
@CrossOrigin(origins = "*")
public class OrderItemController {
    @Autowired
    private OrderItemService orderItemService;

    @GetMapping(value = "/get-all/{currentPage}")
    public ResponseEntity<ResponseObject> getAll(@PathVariable int currentPage
            , @RequestParam(defaultValue = "5") int pageSize
            , @RequestParam(defaultValue = "id") String field) {
        if(currentPage < 1 || pageSize < 1 || currentPage > pageSize){
            return ResponseEntity.ok(new ResponseObject("get success", orderItemService.get()));
        }
        Page<OrderItemResponseDTO> orderItemDTOS = orderItemService.getAll(currentPage, pageSize, field);
        var pageList = PageList.<OrderItemResponseDTO>builder()
                .totalPage(orderItemDTOS.getTotalPages())
                .currentPage(currentPage)
                .listResult(orderItemDTOS.getContent())
                .build();
        return ResponseEntity.ok(new ResponseObject("get success", pageList));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getById(@PathVariable long id) {
        return orderItemService.getById(id);
    }
    @GetMapping("/get-by-order-detail/{orderDetailId}")
    public ResponseEntity<ResponseObject> getByOrderDetailId(@PathVariable long orderDetailId) {
        return orderItemService.getOrderItemsByOrderDetailId(orderDetailId);
    }
    @PostMapping
    public ResponseEntity<ResponseObject> create(@RequestBody OrderItemDTO orderItemDTO) {
        return orderItemService.create(orderItemDTO);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> update(@PathVariable long id,@RequestBody OrderItemDTO orderItemDTO) {
        return orderItemService.update(id,orderItemDTO);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> delete(@PathVariable long id) {
        return orderItemService.delete(id);
    }
}
