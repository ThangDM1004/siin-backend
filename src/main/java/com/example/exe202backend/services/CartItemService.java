package com.example.exe202backend.services;

import com.example.exe202backend.dto.CartItemDTO;
import com.example.exe202backend.mapper.CartItemMapper;
import com.example.exe202backend.models.*;
import com.example.exe202backend.repositories.*;
import com.example.exe202backend.response.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductMaterialService productMaterialService;
    @Autowired
    private ProductMaterialRepository productMaterialRepository;
    @Autowired
    private AccessoryRepository accessoryRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductCategoryRepository productCategoryRepository;
    @Autowired
    private UserRepository userRepository;

    public List<CartItemDTO> get(){
        return cartItemRepository.findAll().stream().map(cartItemMapper::toDto).collect(Collectors.toList());
    }
    public ResponseEntity<ResponseObject> create(CartItemDTO cartItemDTO) {
        CartItem cartItem = cartItemMapper.toEntity(cartItemDTO);
        cartItem.setProduct(productRepository.findById(cartItemDTO.getProductId()).orElse(null));
        cartItem.setCart(cartRepository.findById(cartItemDTO.getCartId()).orElse(null));
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
        if(cartItemDTO.getCartId() == 0){
            cartItemDTO.setCartId(cartItem.getCart().getId());
        }
        if(cartItemDTO.getProductId() == 0){
            cartItemDTO.setProductId(cartItem.getProduct().getId());
        }
        if(cartItemDTO.getQuantity() == 0){
            cartItemDTO.setQuantity(cartItem.getQuantity());
        }
        cartItemDTO.setStatus(cartItem.getStatus());
        cartItemMapper.updateCartItemFromDto(cartItemDTO,cartItem);
        cartItem.setProduct(productRepository.findById(cartItemDTO.getProductId()).orElse(null));
        cartItem.setCart(cartRepository.findById(cartItemDTO.getCartId()).orElse(null));
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

    public ResponseEntity<ResponseObject> fromProductToCartItem(Long accessoryId, String color, String size, int quantity, Long userId) {
        Product product = productService.isExist(accessoryId, color, size);
        ProductMaterial material = productMaterialRepository.findById(productMaterialService.getMaterialIdBySizeAndColorName(color, size)).get();
        Accessory accessory = accessoryRepository.findById(accessoryId).get();
        product = Product.builder()
                .name(accessory.getName() + "-" + material.getColorName() + "-" + material.getSize())
                .price(accessory.getPrice() + material.getPrice())
                .quantity(0)
                .category(productCategoryRepository.findById(1L).get())
                .accessory(accessory)
                .productMaterial(material)
                .build();

        product.setQuantity(quantity);
        productRepository.save(product);

        CartItem cartItem = CartItem.builder()
                .product(product)
                .quantity(quantity)
                .build();

        if (userId == null) {
            return ResponseEntity.ok(new ResponseObject("add success", cartItemMapper.toDto(cartItem)));
        }

        Cart cart = cartRepository.findByUserId(userId);
        if (cart == null) {
            cart = Cart.builder()
                    .total(0)
                    .cartItems(new ArrayList<>())
                    .user(userRepository.findById(userId).get())
                    .build();
        }

        cart.getCartItems().add(cartItem);
        cart.setTotal(cart.getTotal() + product.getPrice() * quantity);
        cartItem.setCart(cart);

        cartRepository.save(cart);
        cartItemRepository.save(cartItem);

        return ResponseEntity.ok(new ResponseObject("add success", cartItemMapper.toDto(cartItem)));
    }
}
