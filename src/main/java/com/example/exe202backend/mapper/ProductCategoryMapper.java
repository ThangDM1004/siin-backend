package com.example.exe202backend.mapper;

import com.example.exe202backend.dto.ProductCategoryDTO;
import com.example.exe202backend.models.ProductCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductCategoryMapper {
    @Mapping(target = "status", source = "status")
    @Mapping(target = "id", source = "id")
    ProductCategoryDTO toDto(ProductCategory productCategory);
    @Mapping(target = "status", source = "status")
    @Mapping(target = "id", source = "id")
    ProductCategory toEntity(ProductCategoryDTO productCategoryDTO);
    @Mapping(target = "status", source = "status")
    @Mapping(target = "id", source = "id")
    void updateProductCategoryFromDto(ProductCategoryDTO productCategoryDTO,
                                      @MappingTarget ProductCategory productCategory);
}
