package com.example.exe202backend.mapper;

import com.example.exe202backend.dto.ColorDTO;
import com.example.exe202backend.models.Color;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ColorMapper {
    @Mapping(target = "id", source = "id")
    ColorDTO toDto(Color color);
    @Mapping(target = "id", source = "id")
    Color toEntity(ColorDTO colorDTO);
}
