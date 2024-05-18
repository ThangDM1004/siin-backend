package com.example.exe202backend.mapper;

import com.example.exe202backend.dto.AccessoryDTO;
import com.example.exe202backend.models.Accessory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AccessoryMapper {
    @Mapping(target = "status", source = "status")
    AccessoryDTO toDto(Accessory accessory);

    @Mapping(target = "status", source = "status")
    Accessory toEntity(AccessoryDTO accessoryDto);

    @Mapping(target = "status", source = "status")
    void updateAccessoryFromDto(AccessoryDTO accessoryDto, @MappingTarget Accessory accessory);
}
