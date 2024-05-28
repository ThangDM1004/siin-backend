package com.example.exe202backend.services;

import com.example.exe202backend.dto.CartDTO;
import com.example.exe202backend.mapper.CartMapper;
import com.example.exe202backend.models.*;
import com.example.exe202backend.repositories.*;
import com.example.exe202backend.response.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductService productService;
    @Autowired
    private AccessoryRepository accessoryRepository;
    @Autowired
    private ProductMaterialService productMaterialService;
    @Autowired
    private ProductMaterialRepository productMaterialRepository;
    @Autowired
    private CartItemRepository cartItemRepository;

    public List<CartDTO> get(){
        return cartRepository.findAll().stream().map(cartMapper::toDto).collect(Collectors.toList());
    }

    public ResponseEntity<ResponseObject> create(CartDTO cartDTO) {
        Cart cart = cartMapper.toEntity(cartDTO);
        cartRepository.save(cart);
        return ResponseEntity.ok(new ResponseObject("create success",cartDTO));
    }
    public Page<CartDTO> getAll(int currentPage, int pageSize, String field){
        Page<Cart> carts = cartRepository.findAll(
                PageRequest.of(currentPage-1,pageSize, Sort.by(Sort.Direction.ASC,field)));
        return carts.map(cartMapper::toDto);
    }
    public ResponseEntity<ResponseObject> getById(long id){
        Cart cart = cartRepository.findById(id).orElseThrow(()->
                new RuntimeException("Cart not found"));
        return ResponseEntity.ok(new ResponseObject("get success",cartMapper.toDto(cart)));
    }
    public ResponseEntity<ResponseObject> delete(long id){
        Optional<Cart> cart = cartRepository.findById(id);
        if(cart.isPresent()){
            cart.get().setStatus(false);
            cartRepository.save(cart.get());
            return ResponseEntity.ok(new ResponseObject("delete success",cartMapper.toDto(cart.get())));
        }
        return ResponseEntity.badRequest().body(new ResponseObject("Cart not found",null));
    }

    public ResponseEntity<ResponseObject> update(Long id,CartDTO cartDTO){
        Cart existingCart = cartRepository.findById(id).orElseThrow(()
                -> new RuntimeException("Cart not found"));
        cartMapper.updateCartFromDto(cartDTO,existingCart);
        cartRepository.save(existingCart);
        return ResponseEntity.ok(new ResponseObject("update success",cartDTO));
    }
}
