package com.example.exe202backend.controllers;

import com.example.exe202backend.dto.CartDTO;
import com.example.exe202backend.dto.PageList;
import com.example.exe202backend.response.ResponseObject;
import com.example.exe202backend.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping(value = "/get-all/{currentPage}")
    public ResponseEntity<ResponseObject> getAllAccessory(@PathVariable int currentPage
            , @RequestParam(defaultValue = "5") int pageSize
            , @RequestParam(defaultValue = "name") String field) {
        Page<CartDTO> accessories = cartService.getAll(currentPage, pageSize, field);
        var pageList = PageList.<CartDTO>builder()
                .totalPage(accessories.getTotalPages())
                .currentPage(currentPage)
                .listResult(accessories.getContent())
                .build();
        return ResponseEntity.ok(new ResponseObject("get success", pageList));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getAccessoryById(@PathVariable long id) {
        return cartService.getById(id);
    }
    @PostMapping
    public ResponseEntity<ResponseObject> createAccessory(@RequestBody CartDTO cartDTO) {
        return cartService.create(cartDTO);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateAccessory(@PathVariable long id,@RequestBody CartDTO cartDTO) {
        return cartService.update(id,cartDTO);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteAccessory(@PathVariable long id) {
        return cartService.delete(id);
    }
}
