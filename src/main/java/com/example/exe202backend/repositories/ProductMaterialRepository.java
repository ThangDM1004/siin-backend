package com.example.exe202backend.repositories;

import com.example.exe202backend.models.ProductMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductMaterialRepository extends JpaRepository<ProductMaterial, Long> {
    @Query("SELECT m FROM ProductMaterial m WHERE m.product.id = ?1")
    List<ProductMaterial> getProductMaterialsByProductId(Long productId);

    @Query("SELECT pm FROM ProductMaterial pm WHERE pm.product.id = ?1 AND pm.size.id = ?2 AND pm.color.id = ?3")
    ProductMaterial getProductMaterials(Long productId, Long sizeId, Long colorId);

}
