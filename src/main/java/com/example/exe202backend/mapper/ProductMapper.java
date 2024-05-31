package com.example.exe202backend.mapper;

import com.example.exe202backend.dto.ProductDTO;
import com.example.exe202backend.models.Product;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "status", source = "status")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "accessoryId", source = "accessory.id")
    @Mapping(target = "materialId", source = "productMaterial.id")
    ProductDTO toDto(Product accessory);

    @Mapping(target = "status", source = "status")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "accessory", ignore = true)
    @Mapping(target = "productMaterial", ignore = true)
    void updateProductFromDto(ProductDTO productDTO, @MappingTarget Product product);

    @Mapping(target = "status", source = "status")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "accessory", ignore = true)
    @Mapping(target = "productMaterial", ignore = true)
    Product toEntity(ProductDTO productDTO);
}
