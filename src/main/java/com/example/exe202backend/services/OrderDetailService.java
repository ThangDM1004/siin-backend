package com.example.exe202backend.services;

import com.example.exe202backend.dto.CartItemResponseDTO_2;
import com.example.exe202backend.dto.OrderDetailDTO;
import com.example.exe202backend.dto.OrderDetailRequestDTO;
import com.example.exe202backend.mapper.OrderDetailMapper;
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
public class OrderDetailService {
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductMaterialRepository productMaterialRepository;

    public List<OrderDetailDTO> get(){
        return orderDetailRepository.findAll().stream().map(orderDetailMapper::toDto).collect(Collectors.toList());
    }

    public ResponseEntity<ResponseObject> create(Long userId, List<CartItemResponseDTO_2> cartItemResponseDTOS, OrderDetailRequestDTO orderDetailRequestDTO) {
        OrderDetailDTO orderDetailDTO = null;
        if (userId != null && cartItemResponseDTOS == null) {
            Cart cart = cartRepository.findByUserId(userId);
            if (cart != null) {
                orderDetailDTO = processOrder(cart,orderDetailRequestDTO);
            } else {
                throw new RuntimeException("Cart can not found");
            }
        }
        else if (userId == null && cartItemResponseDTOS != null) {
                orderDetailDTO= processOrderForGuest(cartItemResponseDTOS, orderDetailRequestDTO);
        }
        return ResponseEntity.ok(new ResponseObject("Create order detail", orderDetailDTO));
    }

    public Page<OrderDetailDTO> getAll(int currentPage, int pageSize, String field){
        Page<OrderDetail> orderDetails = orderDetailRepository.findAll(
                PageRequest.of(currentPage-1,pageSize, Sort.by(Sort.Direction.ASC,field)));
        return orderDetails.map(orderDetailMapper::toDto);
    }

    public ResponseEntity<ResponseObject> getById(long id){
        OrderDetail orderDetail = orderDetailRepository.findById(id).orElseThrow(()->
                new RuntimeException("Order detail not found"));
        return ResponseEntity.ok(new ResponseObject("get success",orderDetailMapper.toDto(orderDetail)));
    }

    public ResponseEntity<ResponseObject> delete(long id){
        Optional<OrderDetail> orderDetail = orderDetailRepository.findById(id);
        if(orderDetail.isPresent()){
            orderDetail.get().setStatus(false);
            orderDetailRepository.save(orderDetail.get());
            return ResponseEntity.ok(new ResponseObject("delete success",orderDetailMapper.toDto(orderDetail.get())));
        }
        return ResponseEntity.badRequest().body(new ResponseObject("Order detail not found",null));
    }

    public ResponseEntity<ResponseObject> update(Long id,OrderDetailDTO orderDetailDTO){
        OrderDetail existingOrderDetail = orderDetailRepository.findById(id).orElseThrow(()
                -> new RuntimeException("Order detail not found"));

        orderDetailDTO.setOrderStatus(orderDetailDTO.getOrderStatus() != null ? orderDetailDTO.getOrderStatus() : existingOrderDetail.getOrderStatus());
        orderDetailDTO.setUserId(orderDetailDTO.getUserId() != 0 ? orderDetailDTO.getUserId() : null);
        orderDetailDTO.setEmail(orderDetailDTO.getEmail() != null ? orderDetailDTO.getEmail() : existingOrderDetail.getEmail());
        orderDetailDTO.setPhone(orderDetailDTO.getPhone() != null ? orderDetailDTO.getPhone() : existingOrderDetail.getPhone());
        orderDetailDTO.setAddress(orderDetailDTO.getAddress() != null ? orderDetailDTO.getAddress() : existingOrderDetail.getAddress());
        orderDetailDTO.setDistrict(orderDetailDTO.getDistrict() != null ? orderDetailDTO.getDistrict() : existingOrderDetail.getDistrict());
        orderDetailDTO.setProvince(orderDetailDTO.getProvince() != null ? orderDetailDTO.getProvince() : existingOrderDetail.getProvince());
        orderDetailDTO.setWard(orderDetailDTO.getWard() != null ? orderDetailDTO.getWard() : existingOrderDetail.getWard());
        orderDetailDTO.setNameCustomer(orderDetailDTO.getNameCustomer() != null ? orderDetailDTO.getNameCustomer() : existingOrderDetail.getNameCustomer());
        orderDetailDTO.setNote(orderDetailDTO.getNote() != null ? orderDetailDTO.getNote() : existingOrderDetail.getNote());
        orderDetailDTO.setTotal(orderDetailDTO.getTotal() != 0 ? orderDetailDTO.getTotal() : existingOrderDetail.getTotal());

        orderDetailDTO.setStatus(existingOrderDetail.getStatus());
        orderDetailMapper.updateOrderDetailFromDto(orderDetailDTO,existingOrderDetail);
        existingOrderDetail.setUserModel(userRepository.findById(orderDetailDTO.getUserId()).orElse(null));
        orderDetailRepository.save(existingOrderDetail);
        return ResponseEntity.ok(new ResponseObject("update success",orderDetailDTO));
    }

    private OrderDetailDTO processOrder(Cart cart, OrderDetailRequestDTO orderDetailRequestDTO) {
        OrderDetail orderDetail = orderDetailMapper.toEntity(orderDetailRequestDTO);
        orderDetail.setUserModel(cart.getUser());
        orderDetail.setTotal(cart.getTotal());
        orderDetail.setOrderStatus("Pending");

        orderDetailRepository.save(orderDetail);

        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductMaterial(cartItem.getProductMaterial());
            orderItem.setOrderDetail(orderDetail);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProductMaterial().getPrice());

            orderItemRepository.save(orderItem);
        }
        return orderDetailMapper.toDto(orderDetail);
    }
    private OrderDetailDTO processOrderForGuest(List<CartItemResponseDTO_2> cartItemResponseDTOS, OrderDetailRequestDTO orderDetailRequestDTO) {
        OrderDetail orderDetail = orderDetailMapper.toEntity(orderDetailRequestDTO);
        orderDetail.setOrderStatus("Pending");
        double total = 0;
        List<CartItem> cartItems = new ArrayList<>();
        for (CartItemResponseDTO_2 cartItemResponseDTO : cartItemResponseDTOS) {
            cartItems.add(CartItem.builder()
                            .quantity(cartItemResponseDTO.getQuantity())
                            .status(true)
                            .productMaterial(productMaterialRepository.findById(cartItemResponseDTO.getProductMaterialId()).get())
                            .build());
        }
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductMaterial(cartItem.getProductMaterial());
            orderItem.setOrderDetail(orderDetail);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProductMaterial().getPrice());
            orderItem.setStatus(true);
            orderItems.add(orderItem);
            total += cartItem.getProductMaterial().getPrice();
        }
        orderDetail.setTotal(total);
        orderDetailRepository.save(orderDetail);
        orderItemRepository.saveAll(orderItems);
        return orderDetailMapper.toDto(orderDetail);
    }
}
