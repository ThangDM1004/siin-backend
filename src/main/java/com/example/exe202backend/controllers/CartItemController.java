package com.example.exe202backend.controllers;

import com.example.exe202backend.dto.CartItemDTO;
import com.example.exe202backend.dto.PageList;
import com.example.exe202backend.response.ResponseObject;
import com.example.exe202backend.services.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart-item")
public class CartItemController {
    @Autowired
    private CartItemService cartItemService;

    @GetMapping(value = "/get-all/{currentPage}")
    public ResponseEntity<ResponseObject> getAllAccessory(@PathVariable int currentPage
            , @RequestParam(defaultValue = "5") int pageSize
            , @RequestParam(defaultValue = "id") String field) {
        Page<CartItemDTO> accessories = cartItemService.getAll(currentPage, pageSize, field);
        var pageList = PageList.<CartItemDTO>builder()
                .totalPage(accessories.getTotalPages())
                .currentPage(currentPage)
                .listResult(accessories.getContent())
                .build();
        return ResponseEntity.ok(new ResponseObject("get success", pageList));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getAccessoryById(@PathVariable long id) {
        return cartItemService.getById(id);
    }
    @PostMapping
    public ResponseEntity<ResponseObject> createAccessory(@RequestBody CartItemDTO cartItemDTO) {
        return cartItemService.create(cartItemDTO);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateAccessory(@PathVariable long id,@RequestBody CartItemDTO cartItemDTO) {
        return cartItemService.update(id,cartItemDTO);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteAccessory(@PathVariable long id) {
        return cartItemService.delete(id);
    }
}
