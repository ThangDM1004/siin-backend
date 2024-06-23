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
    private UserRepository userRepository;

    public List<CartDTO> get(){
        return cartRepository.findAll().stream().map(cartMapper::toDto).collect(Collectors.toList());
    }

    public ResponseEntity<ResponseObject> create(CartDTO cartDTO) {
        Cart cart = cartMapper.toEntity(cartDTO);
        cart.setUser(userRepository.findById(cartDTO.getUserId()).orElse(null));
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
            UserModel user = userRepository.findUserByCartId(id).get();
            user.setCart(null);
            userRepository.save(user);
            cartRepository.delete(cart.get());
            return ResponseEntity.ok(new ResponseObject("delete success",cartMapper.toDto(cart.get())));
        }
        return ResponseEntity.badRequest().body(new ResponseObject("Cart not found",null));
    }

    public ResponseEntity<ResponseObject> update(Long id,CartDTO cartDTO){
        Cart existingCart = cartRepository.findById(id).orElseThrow(()
                -> new RuntimeException("Cart not found"));
        if(cartDTO.getTotal() == 0){
            cartDTO.setTotal(existingCart.getTotal());
        }
        if(cartDTO.getUserId() == 0){
            cartDTO.setUserId(existingCart.getUser().getId());
        }
        cartDTO.setStatus(existingCart.getStatus());
        cartMapper.updateCartFromDto(cartDTO,existingCart);
        existingCart.setUser(userRepository.findById(cartDTO.getUserId()).orElse(null));
        cartRepository.save(existingCart);
        return ResponseEntity.ok(new ResponseObject("update success",cartDTO));
    }
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }
}
