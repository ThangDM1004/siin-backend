package com.example.exe202backend.mapper;

import com.example.exe202backend.dto.ProductSubImageDTO;
import com.example.exe202backend.models.Product;
import com.example.exe202backend.models.ProductSubImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ProductSubImageMapper {
    @Mapping(target = "status", source = "status")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "productId", source = "product.id")
    ProductSubImageDTO toDto(ProductSubImage productSubImage);
    @Mapping(target = "status", source = "status")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "product", source = "productId", qualifiedByName = "productFromId")
    ProductSubImage toEntity(ProductSubImageDTO productSubImageDTO);
    @Mapping(target = "status", source = "status")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "product", source = "productId", qualifiedByName = "productFromId")
    void updateProductSubImageFromDto(ProductSubImageDTO productSubImageDTO, @MappingTarget ProductSubImage productSubImage);

    @Named("productFromId")
    default Product productFromId(long id) {
        Product product = new Product();
        product.setId(id);
        return product;
    }
}
