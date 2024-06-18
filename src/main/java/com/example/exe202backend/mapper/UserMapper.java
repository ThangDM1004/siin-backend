package com.example.exe202backend.mapper;

import com.example.exe202backend.dto.CreateUserDTO;
import com.example.exe202backend.dto.UserDTO;
import com.example.exe202backend.models.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", source = "id")
    UserDTO toDto(UserModel user);

    @Mapping(target = "addresses",ignore = true)
    @Mapping(target = "cart",ignore = true)
    @Mapping(target = "orderDetails",ignore = true)
    @Mapping(target = "id", source = "id")
    UserModel toEntity(UserDTO userDTO);

    @Mapping(target = "addresses",ignore = true)
    @Mapping(target = "cart",ignore = true)
    @Mapping(target = "orderDetails",ignore = true)
    @Mapping(target = "id", source = "id")
    void updateUserFromDto(UserDTO userDTO,@MappingTarget UserModel user);

    @Mapping(target = "id", source = "id")
    CreateUserDTO toCreateDto(UserModel user);

    @Mapping(target = "addresses",ignore = true)
    @Mapping(target = "cart",ignore = true)
    @Mapping(target = "orderDetails",ignore = true)
    @Mapping(target = "id", source = "id")
    UserModel createToEntity(CreateUserDTO userDTO);
}
