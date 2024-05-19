package com.example.exe202backend.controllers;

import com.example.exe202backend.dto.CartItemDTO;
import com.example.exe202backend.dto.OrderItemDTO;
import com.example.exe202backend.dto.PageList;
import com.example.exe202backend.response.ResponseObject;
import com.example.exe202backend.services.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/order-item")
public class OrderItemController {
    @Autowired
    private OrderItemService orderItemService;

    @GetMapping(value = "/get-all/{currentPage}")
    public ResponseEntity<ResponseObject> getAllAccessory(@PathVariable int currentPage
            , @RequestParam(defaultValue = "5") int pageSize
            , @RequestParam(defaultValue = "id") String field) {
        Page<OrderItemDTO> orderItemDTOS = orderItemService.getAll(currentPage, pageSize, field);
        var pageList = PageList.<OrderItemDTO>builder()
                .totalPage(orderItemDTOS.getTotalPages())
                .currentPage(currentPage)
                .listResult(orderItemDTOS.getContent())
                .build();
        return ResponseEntity.ok(new ResponseObject("get success", pageList));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getAccessoryById(@PathVariable long id) {
        return orderItemService.getById(id);
    }
    @PostMapping
    public ResponseEntity<ResponseObject> createAccessory(@RequestBody OrderItemDTO orderItemDTO) {
        return orderItemService.create(orderItemDTO);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateAccessory(@PathVariable long id,@RequestBody OrderItemDTO orderItemDTO) {
        return orderItemService.update(id,orderItemDTO);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteAccessory(@PathVariable long id) {
        return orderItemService.delete(id);
    }
}
