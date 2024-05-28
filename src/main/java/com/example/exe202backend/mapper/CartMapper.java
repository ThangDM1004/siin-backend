package com.example.exe202backend.mapper;

import com.example.exe202backend.dto.CartDTO;
import com.example.exe202backend.models.Cart;
import com.example.exe202backend.models.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface CartMapper {
    @Mapping(target = "status", source = "status")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "userId", source = "user.id")
    CartDTO toDto(Cart cart);

    @Mapping(target = "status", source = "status")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "user", source = "userId", qualifiedByName = "userModelFromId")
    Cart toEntity(CartDTO cartDTO);

    @Mapping(target = "status", source = "status")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "user", source = "userId", qualifiedByName = "userModelFromId")
    void updateCartFromDto(CartDTO cartDTO, @MappingTarget Cart cart);

    @Named("userModelFromId")
    default UserModel userModelFromId(long id) {
        UserModel userModel = new UserModel();
        userModel.setId(id);
        return userModel;
    }
}
