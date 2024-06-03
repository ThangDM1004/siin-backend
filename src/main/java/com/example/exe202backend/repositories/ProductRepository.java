package com.example.exe202backend.repositories;

import com.example.exe202backend.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p.id FROM Product p WHERE LOWER(p.category.name) = LOWER(:categoryName)")
    List<Long> findProductIdsByCategoryName(@Param("categoryName") String categoryName);
}
