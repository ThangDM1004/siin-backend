package com.example.exe202backend.mapper;

import com.example.exe202backend.dto.UserAddressDTO;
import com.example.exe202backend.models.UserAddress;
import com.example.exe202backend.models.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserAddressMapper {
    @Mapping(target = "userId", source = "userModel.id")
    @Mapping(target = "id", source = "id")
    UserAddressDTO toDto(UserAddress userAddress);

    @Mapping(target = "userModel",ignore = true)
    @Mapping(target = "id", source = "id")
    UserAddress toEntity(UserAddressDTO userAddressDTO);

    @Mapping(target = "userModel", ignore = true)
    @Mapping(target = "id", source = "id")
    void updateUserAddressFromDto(UserAddressDTO userAddressDTO, @MappingTarget UserAddress userAddress);
}
