package com.example.exe202backend.mapper;

import com.example.exe202backend.dto.ColorDTO;
import com.example.exe202backend.dto.SizeDTO;
import com.example.exe202backend.models.Color;
import com.example.exe202backend.models.Size;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SizeMapper {
    @Mapping(target = "id", source = "id")
    SizeDTO toDto(Size size);
    @Mapping(target = "id", source = "id")
    Size toEntity(SizeDTO sizeDTO);
}
