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
@CrossOrigin(origins = "*")
public class CartItemController {
    @Autowired
    private CartItemService cartItemService;

    @GetMapping(value = "/get-all/{currentPage}")
    public ResponseEntity<ResponseObject> getAll(@PathVariable int currentPage
            , @RequestParam(defaultValue = "5") int pageSize
            , @RequestParam(defaultValue = "id") String field) {
        if(currentPage < 1 || pageSize < 1 || currentPage > pageSize){
            return ResponseEntity.ok(new ResponseObject("get success", cartItemService.get()));
        }
        Page<CartItemDTO> accessories = cartItemService.getAll(currentPage, pageSize, field);
        var pageList = PageList.<CartItemDTO>builder()
                .totalPage(accessories.getTotalPages())
                .currentPage(currentPage)
                .listResult(accessories.getContent())
                .build();
        return ResponseEntity.ok(new ResponseObject("get success", pageList));
    }
    @GetMapping(value = "/get-by-user/{userId}")
    public ResponseEntity<ResponseObject> getByUser(@PathVariable  Long userId) {
        return cartItemService.getCartItemsByUserId(userId);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getById(@PathVariable long id) {
        return cartItemService.getById(id);
    }
    @PostMapping
    public ResponseEntity<ResponseObject> create(@RequestBody CartItemDTO cartItemDTO) {
        return cartItemService.create(cartItemDTO);
    }
//    @PostMapping("/add-to-cart")
//    public ResponseEntity<ResponseObject> addToCart(
//            @RequestParam Long accessoryId,
//            @RequestParam String color,
//            @RequestParam String size,
//            @RequestParam int quantity,
//            @RequestParam(required = false) Long userId
//    ) {
//        return cartItemService.fromProductToCartItem(accessoryId, color, size, quantity, userId);
//    }
    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> update(@PathVariable long id,@RequestBody CartItemDTO cartItemDTO) {
        return cartItemService.update(id,cartItemDTO);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> delete(@PathVariable long id) {
        return cartItemService.delete(id);
    }
}
