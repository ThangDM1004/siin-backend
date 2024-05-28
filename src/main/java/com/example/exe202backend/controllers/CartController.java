package com.example.exe202backend.controllers;

import com.example.exe202backend.dto.CartDTO;
import com.example.exe202backend.dto.PageList;
import com.example.exe202backend.response.ResponseObject;
import com.example.exe202backend.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/cart")
@CrossOrigin(origins = "*")
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping(value = "/get-all/{currentPage}")
    public ResponseEntity<ResponseObject> getALl(@PathVariable int currentPage
            , @RequestParam(defaultValue = "5") int pageSize
            , @RequestParam(defaultValue = "id") String field) {
        if(currentPage < 1 || pageSize < 1 || currentPage > pageSize){
            return ResponseEntity.ok(new ResponseObject("get success", cartService.get()));
        }
        Page<CartDTO> accessories = cartService.getAll(currentPage, pageSize, field);
        var pageList = PageList.<CartDTO>builder()
                .totalPage(accessories.getTotalPages())
                .currentPage(currentPage)
                .listResult(accessories.getContent())
                .build();
        return ResponseEntity.ok(new ResponseObject("get success", pageList));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getById(@PathVariable long id) {
        return cartService.getById(id);
    }
    @PostMapping
    public ResponseEntity<ResponseObject> create(@RequestBody CartDTO cartDTO) {
        return cartService.create(cartDTO);
    }
    @PostMapping("/add-to-cart")
    public ResponseEntity<ResponseObject> addToCart(
            @RequestParam long accessoryId,
            @RequestParam String size,
            @RequestParam String colorName,
            @RequestParam int quantity,
            @RequestParam String sessionId,
            @RequestParam LocalDateTime expirationTime) {

        ResponseEntity<ResponseObject> response;

        try {
            ResponseEntity<ResponseObject> responseEntity = cartService.addToCart(accessoryId, size, colorName, quantity, sessionId, expirationTime);
            response = ResponseEntity.status(responseEntity.getStatusCode()).body(responseEntity.getBody());
        } catch (Exception e) {
            response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseObject("Internal Server Error", null));
        }

        return response;
    }
    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> update(@PathVariable long id,@RequestBody CartDTO cartDTO) {
        return cartService.update(id,cartDTO);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> delete(@PathVariable long id) {
        return cartService.delete(id);
    }

}
