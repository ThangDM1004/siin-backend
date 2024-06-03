package com.example.exe202backend.mapper;

import com.example.exe202backend.dto.ProductDTO;
import com.example.exe202backend.models.Product;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "status", source = "status")
    @Mapping(target = "id", source = "id")
    ProductDTO toDto(Product accessory);

    @Mapping(target = "status", source = "status")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "accessory", ignore = true)
    void updateProductFromDto(ProductDTO productDTO, @MappingTarget Product product);

    @Mapping(target = "status", source = "status")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "accessory", ignore = true)
    Product toEntity(ProductDTO productDTO);
}
