package com.example.exe202backend.mapper;

import com.example.exe202backend.dto.CartItemDTO;
import com.example.exe202backend.models.Cart;
import com.example.exe202backend.models.CartItem;
import com.example.exe202backend.models.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
    @Mapping(target = "status", source = "status")
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "cartId", source = "cart.id")
    CartItemDTO toDto(CartItem cartItem);

    @Mapping(target = "status", source = "status")
    @Mapping(target = "product", source = "productId", qualifiedByName = "productFromId")
    @Mapping(target = "cart", source = "cartId", qualifiedByName = "cartFromId")
    CartItem toEntity(CartItemDTO cartItemDTO);

    @Mapping(target = "status", source = "status")
    @Mapping(target = "product", source = "productId", qualifiedByName = "productFromId")
    @Mapping(target = "cart", source = "cartId", qualifiedByName = "cartFromId")
    void updateCartItemFromDto(CartItemDTO cartItemDTO, @MappingTarget CartItem cartItem);

    @Named("productFromId")
    default Product productFromId(long id) {
        Product product = new Product();
        product.setId(id);
        return product;
    }
    @Named("cartFromId")
    default Cart cartFromId(long id) {
        Cart cart = new Cart();
        cart.setId(id);
        return cart;
    }
}
