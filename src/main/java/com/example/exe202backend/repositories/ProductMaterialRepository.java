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

}
