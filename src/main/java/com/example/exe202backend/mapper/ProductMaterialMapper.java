package com.example.exe202backend.mapper;

import com.example.exe202backend.dto.ProductMaterialDTO;
import com.example.exe202backend.models.ProductMaterial;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMaterialMapper {
    ProductMaterialDTO toDto(ProductMaterial productMaterial);

    ProductMaterial toEntity(ProductMaterialDTO productMaterialDTO);

    void updateProductMaterialFromDto(ProductMaterialDTO productMaterialDTO,
                                      @MappingTarget ProductMaterial productMaterial);
}
