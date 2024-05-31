package com.example.exe202backend.services;

import com.example.exe202backend.dto.CartItemDTO;
import com.example.exe202backend.mapper.CartItemMapper;
import com.example.exe202backend.models.Cart;
import com.example.exe202backend.models.CartItem;
import com.example.exe202backend.repositories.CartItemRepository;
import com.example.exe202backend.response.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartItemService {
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private CartItemMapper cartItemMapper;
    @Autowired
    private CartService cartService;

    public List<CartItemDTO> get(){
        return cartItemRepository.findAll().stream().map(cartItemMapper::toDto).collect(Collectors.toList());
    }
    public ResponseEntity<ResponseObject> create(CartItemDTO cartItemDTO) {
        CartItem cartItem = cartItemMapper.toEntity(cartItemDTO);
        cartItemRepository.save(cartItem);
        return ResponseEntity.ok(new ResponseObject("create success",cartItemDTO));
    }

    public Page<CartItemDTO> getAll(int currentPage, int pageSize, String field){
        Page<CartItem> cartItems = cartItemRepository.findAll(
                PageRequest.of(currentPage-1,pageSize, Sort.by(Sort.Direction.ASC,field)));
        return cartItems.map(cartItemMapper::toDto);
    }

    public ResponseEntity<ResponseObject> getById(long id){
        CartItem cartItem = cartItemRepository.findById(id).orElseThrow(()->
                new RuntimeException("Cart Item not found"));
        return ResponseEntity.ok(new ResponseObject("get success",cartItemMapper.toDto(cartItem)));
    }
    public ResponseEntity<ResponseObject> delete(long id){
        Optional<CartItem> cartItem = cartItemRepository.findById(id);
        if(cartItem.isPresent()){
            cartItem.get().setStatus(false);
            cartItemRepository.save(cartItem.get());
            return ResponseEntity.ok(new ResponseObject("delete success",cartItemMapper.toDto(cartItem.get())));
        }
        return ResponseEntity.badRequest().body(new ResponseObject("Cart Item not found",null));
    }

    public ResponseEntity<ResponseObject> update(Long id,CartItemDTO cartItemDTO){
        CartItem cartItem = cartItemRepository.findById(id).orElseThrow(()->
                new RuntimeException("Cart Item not found"));
        cartItemMapper.updateCartItemFromDto(cartItemDTO,cartItem);
        cartItemRepository.save(cartItem);
        return ResponseEntity.ok(new ResponseObject("update success",cartItemDTO));
    }
    public ResponseEntity<ResponseObject> getCartItemsByUserId(Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        if(cart == null){
            return ResponseEntity.ok(new ResponseObject("Cart not found",null));
        }
        List<CartItemDTO> cartItems = cartItemRepository.findByCartId(cart.getId())
                .stream().map(cartItemMapper::toDto).toList();
        if(cartItems.isEmpty()){return ResponseEntity.ok(new ResponseObject("Not found",cartItems));}
        return ResponseEntity.ok(new ResponseObject("get success",cartItems));
    }
}
