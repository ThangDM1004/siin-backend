package com.example.exe202backend.mapper;

import com.example.exe202backend.dto.ProductMaterialDTO;
import com.example.exe202backend.models.ProductMaterial;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMaterialMapper {
    @Mapping(target = "status", source = "status")
    ProductMaterialDTO toDto(ProductMaterial productMaterial);
    @Mapping(target = "status", source = "status")
    ProductMaterial toEntity(ProductMaterialDTO productMaterialDTO);
    @Mapping(target = "status", source = "status")
    void updateProductMaterialFromDto(ProductMaterialDTO productMaterialDTO,
                                      @MappingTarget ProductMaterial productMaterial);
}
