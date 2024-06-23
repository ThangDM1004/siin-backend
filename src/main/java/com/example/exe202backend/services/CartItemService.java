package com.example.exe202backend.services;

import com.example.exe202backend.dto.CartItemDTO;
import com.example.exe202backend.dto.CartItemResponseDTO;
import com.example.exe202backend.dto.CartItemResponseDTO_2;
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
    private CartRepository cartRepository;
    @Autowired
    private ProductMaterialRepository productMaterialRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ColorRepository colorRepository;
    @Autowired
    private SizeRepository sizeRepository;
    @Autowired
    private ProductMaterialService productMaterialService;
    @Autowired
    private AccessoryRepository accessoryRepository;
    @Autowired
    private UserRepository userRepository;

    public List<CartItemResponseDTO> get(){
        return cartItemRepository.findAll().stream().map(cartItemMapper::toResponseDto).collect(Collectors.toList());
    }
    public ResponseEntity<ResponseObject> create(Long productId, Long colorId, Long sizeId, Long accessoryId,
                                                 int quantity, Long userId) {
        if(productId == null){
            productId = productRepository.findProductIdsByName("customize").get(0);
        }
        Long materialId = productMaterialService.getMaterialIdBySizeAndColorAndProduct(productId,
                colorId,
                sizeId,
                accessoryId);
        if (materialId == null) {
            Product product = null;
            Accessory accessory = null;

            Color color = colorRepository.findById(colorId).orElseThrow(() -> new RuntimeException("Color not found"));
            Size size = sizeRepository.findById(sizeId).orElseThrow(() -> new RuntimeException("Size not found"));
            ProductMaterial newProductMaterial = new ProductMaterial();
            newProductMaterial.setColor(color);
            newProductMaterial.setSize(size);
            newProductMaterial.setQuantity(quantity);

            newProductMaterial.setStatus(true);
            if(productId != null){
                product = productRepository.findById(productId).get();
                newProductMaterial.setProduct(product);
                newProductMaterial.setPrice(product.getPrice()+color.getPrice()+size.getPrice());
            }
            if(accessoryId != null){
                accessory = accessoryRepository.findById(accessoryId).get();
                newProductMaterial.setAccessory(accessory);
                newProductMaterial.setPrice(color.getPrice()+size.getPrice()+accessory.getPrice());
            }
            productMaterialRepository.save(newProductMaterial);
        }
        ProductMaterial productMaterial = productMaterialRepository.findById(
                productMaterialService.getMaterialIdBySizeAndColorAndProduct(productId, colorId, sizeId, accessoryId)
        ).get();
        Cart cart = cartRepository.findByUserId(userId);
        CartItem cartItem;

        //guest
        if (userId == null) {
            cartItem = CartItem.builder()
                    .quantity(quantity)
                    .productMaterial(productMaterial)
                    .status(true)
                    .build();
            CartItemResponseDTO_2 cartItemResponseDTO  = cartItemMapper.toResponseDto_2(cartItem);
            cartItemResponseDTO.setProductMaterialId(productMaterial.getId());
            return ResponseEntity.ok(new ResponseObject("add success", cartItemResponseDTO));
        }

        //user
        if (cart == null) {

            cart = Cart.builder()
                    .total(0)
                    .cartItems(new ArrayList<>())
                    .user(userRepository.findById(userId).get())
                    .build();
            cartRepository.save(cart);
        }
        cartItem = cartItemRepository.findByCartIdAndMaterialId(cartRepository.findByUserId(userId).getId(),
                productMaterial.getId());

        if(cartItem == null){
            cartItem = CartItem.builder()
                    .quantity(quantity)
                    .productMaterial(productMaterial)
                    .status(true)
                    .build();
        }else{
            cartItem.setQuantity(cartItem.getQuantity()+quantity);
        }




        cart.getCartItems().add(cartItem);
        cart.setTotal(cart.getTotal() + productMaterial.getPrice() * quantity);
        cartItem.setCart(cart);

        cartRepository.save(cart);

        return ResponseEntity.ok(new ResponseObject("add success", cartItemMapper.toResponseDto(cartItem)));

    }


    public Page<CartItemResponseDTO> getAll(int currentPage, int pageSize, String field){
        Page<CartItem> cartItems = cartItemRepository.findAll(
                PageRequest.of(currentPage-1,pageSize, Sort.by(Sort.Direction.ASC,field)));
        return cartItems.map(cartItemMapper::toResponseDto);
    }

    public ResponseEntity<ResponseObject> getById(long id){
        Optional<CartItem> cartItem = cartItemRepository.findById(id);
        return cartItem.map(item -> ResponseEntity.ok(new ResponseObject("get success", cartItemMapper.toResponseDto(item))))
                .orElseGet(() -> ResponseEntity.ok(new ResponseObject("get success", null)));
    }
    public ResponseEntity<ResponseObject> delete(long id){
        Optional<CartItem> cartItem = cartItemRepository.findById(id);
        if(cartItem.isPresent()){
            cartItemRepository.delete(cartItem.get());
            return ResponseEntity.ok(new ResponseObject("delete success",cartItemMapper.toResponseDto(cartItem.get())));
        }
        return ResponseEntity.ok(new ResponseObject("Cart Item not found",null));
    }

    public ResponseEntity<ResponseObject> update(Long id,CartItemDTO cartItemDTO){
        CartItem cartItem = cartItemRepository.findById(id).orElseThrow(()->
                new RuntimeException("Cart Item not found"));
        if(cartItemDTO.getCartId() == 0){
            cartItemDTO.setCartId(cartItem.getCart().getId());
        }
        if(cartItemDTO.getProductMaterialId() == 0){
            cartItemDTO.setProductMaterialId(cartItem.getProductMaterial().getId());
        }
        if(cartItemDTO.getQuantity() == 0){
            cartItemDTO.setQuantity(cartItem.getQuantity());
        }
        cartItemDTO.setStatus(cartItem.getStatus());
        cartItemMapper.updateCartItemFromDto(cartItemDTO,cartItem);
        cartItem.setProductMaterial(productMaterialRepository.findById(cartItemDTO.getProductMaterialId()).orElse(null));
        cartItem.setCart(cartRepository.findById(cartItemDTO.getCartId()).orElse(null));
        cartItemRepository.save(cartItem);
        return ResponseEntity.ok(new ResponseObject("update success",cartItemMapper.toResponseDto(cartItem)));
    }
    public ResponseEntity<ResponseObject> getCartItemsByUserId(Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        if(cart == null){
            return ResponseEntity.ok(new ResponseObject("Cart not found",null));
        }
        List<CartItemResponseDTO> cartItems = cartItemRepository.findByCartId(cart.getId())
                .stream().map(cartItemMapper::toResponseDto).toList();
        if(cartItems.isEmpty()){return ResponseEntity.ok(new ResponseObject("Not found",null));}
        return ResponseEntity.ok(new ResponseObject("get success",cartItems));
    }
}
