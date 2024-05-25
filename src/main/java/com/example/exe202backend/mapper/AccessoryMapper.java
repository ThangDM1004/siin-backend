package com.example.exe202backend.mapper;

import com.example.exe202backend.dto.AccessoryDTO;
import com.example.exe202backend.models.Accessory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AccessoryMapper {
    @Mapping(target = "status", source = "status")
    @Mapping(target = "id", source = "id")
    AccessoryDTO toDto(Accessory accessory);

    @Mapping(target = "status", source = "status")
    @Mapping(target = "id", source = "id")
    Accessory toEntity(AccessoryDTO accessoryDto);

    @Mapping(target = "status", source = "status")
    @Mapping(target = "id", source = "id")
    void updateAccessoryFromDto(AccessoryDTO accessoryDto, @MappingTarget Accessory accessory);
}
