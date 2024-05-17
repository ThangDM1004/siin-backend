package com.example.exe202backend.mapper;

import com.example.exe202backend.dto.AccessoryDTO;
import com.example.exe202backend.models.Accessory;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AccessoryMapper {
    AccessoryDTO toDto(Accessory accessory);

    Accessory toEntity(AccessoryDTO accessoryDto);

    void updateAccessoryFromDto(AccessoryDTO accessoryDto, @MappingTarget Accessory accessory);
}
