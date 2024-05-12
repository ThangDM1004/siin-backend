package com.example.exe202backend.repositories;

import com.example.exe202backend.models.ProductMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductMaterialRepository extends JpaRepository<ProductMaterial, Long> {
}
