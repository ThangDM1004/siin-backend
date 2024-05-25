package com.example.exe202backend.mapper;

import com.example.exe202backend.dto.ProductMaterialDTO;
import com.example.exe202backend.models.ProductMaterial;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMaterialMapper {
    @Mapping(target = "status", source = "status")
    @Mapping(target = "id", source = "id")
    ProductMaterialDTO toDto(ProductMaterial productMaterial);
    @Mapping(target = "status", source = "status")
    @Mapping(target = "id", source = "id")
    ProductMaterial toEntity(ProductMaterialDTO productMaterialDTO);
    @Mapping(target = "status", source = "status")
    @Mapping(target = "id", source = "id")
    void updateProductMaterialFromDto(ProductMaterialDTO productMaterialDTO,
                                      @MappingTarget ProductMaterial productMaterial);
}
