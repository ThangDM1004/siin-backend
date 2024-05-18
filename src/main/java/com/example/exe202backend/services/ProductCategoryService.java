package com.example.exe202backend.services;

import com.example.exe202backend.dto.ProductCategoryDTO;
import com.example.exe202backend.mapper.ProductCategoryMapper;
import com.example.exe202backend.models.ProductCategory;
import com.example.exe202backend.repositories.ProductCategoryRepository;
import com.example.exe202backend.response.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ProductCategoryService {
    @Autowired
    private ProductCategoryRepository productCategoryRepository;
    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    public ResponseEntity<ResponseObject> create(ProductCategoryDTO productCategoryDTO) {
        ProductCategory productCategory = productCategoryMapper.toEntity(productCategoryDTO);
        productCategoryRepository.save(productCategory);
        return ResponseEntity.ok(new ResponseObject("create success",productCategoryDTO));
    }

    public Page<ProductCategoryDTO> getAll(int currentPage, int pageSize, String field){
        Page<ProductCategory> productCategoryPage = productCategoryRepository.findAll(
                PageRequest.of(currentPage - 1, pageSize, Sort.by(Sort.Direction.ASC, field)));

        return productCategoryPage.map(productCategoryMapper::toDto);
    }
}
