package com.example.exe202backend.repositories;

import com.example.exe202backend.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p.id FROM Product p WHERE LOWER(p.name) = LOWER(:productName)")
    List<Long> findProductIdsByName(@Param("productName") String productName);
    @Query("SELECT p FROM Product p WHERE p.category.id = :id")
    Page<Product> searchByEmail(@Param("id") long id, Pageable pageable);
    List<Product> findAllByCategory_Id(long categoryId);
}
