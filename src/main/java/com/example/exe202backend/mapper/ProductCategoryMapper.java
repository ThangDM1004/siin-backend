package com.example.exe202backend.mapper;

import com.example.exe202backend.dto.ProductCategoryDTO;
import com.example.exe202backend.models.ProductCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductCategoryMapper {
    @Mapping(target = "status", source = "status")
    ProductCategoryDTO toDto(ProductCategory productCategory);
    @Mapping(target = "status", source = "status")
    ProductCategory toEntity(ProductCategoryDTO productCategoryDTO);
    @Mapping(target = "status", source = "status")
    void updateProductCategoryFromDto(ProductCategoryDTO productCategoryDTO,
                                      @MappingTarget ProductCategory productCategory);
}
