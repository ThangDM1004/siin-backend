package com.example.exe202backend.repositories;

import com.example.exe202backend.models.ProductSubImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface ProductSubImageRepository extends JpaRepository<ProductSubImage, Long> {
    ArrayList<ProductSubImage> findProductSubImageByProduct_Id(Long id);
}
