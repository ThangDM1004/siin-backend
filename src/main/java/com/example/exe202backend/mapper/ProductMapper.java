package com.example.exe202backend.mapper;

import com.example.exe202backend.dto.ProductDTO;
import com.example.exe202backend.models.Accessory;
import com.example.exe202backend.models.Product;
import com.example.exe202backend.models.ProductCategory;
import com.example.exe202backend.models.ProductMaterial;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "status", source = "status")
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "accessoryId", source = "accessory.id")
    @Mapping(target = "materialId", source = "productMaterial.id")
    ProductDTO toDto(Product accessory);

    @Mapping(target = "status", source = "status")
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "accessory", ignore = true)
    @Mapping(target = "productMaterial", ignore = true)
    void updateProductFromDto(ProductDTO productDTO, @MappingTarget Product product);

    @Mapping(target = "status", source = "status")
    @Mapping(target = "category", source = "categoryId", qualifiedByName = "mapToCategory")
    @Mapping(target = "accessory", source = "accessoryId", qualifiedByName = "mapToAccessory")
    @Mapping(target = "productMaterial", source = "materialId", qualifiedByName = "mapToProductMaterial")
    Product toEntity(ProductDTO productDTO);

    @Named("mapToCategory")
    default ProductCategory mapToCategory(Long categoryId) {
        if (categoryId == null) return null;
        ProductCategory category = new ProductCategory();
        category.setId(categoryId);
        return category;
    }

    @Named("mapToAccessory")
    default Accessory mapToAccessory(Long accessoryId) {
        if (accessoryId == null) return null;
        Accessory accessory = new Accessory();
        accessory.setId(accessoryId);
        return accessory;
    }

    @Named("mapToProductMaterial")
    default ProductMaterial mapToProductMaterial(Long materialId) {
        if (materialId == null) return null;
        ProductMaterial material = new ProductMaterial();
        material.setId(materialId);
        return material;
    }

}
