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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductCategoryService {
    @Autowired
    private ProductCategoryRepository productCategoryRepository;
    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    public List<ProductCategoryDTO> get(){
        return productCategoryRepository.findAll().stream().map(productCategoryMapper::toDto).collect(Collectors.toList());
    }

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

    public ResponseEntity<ResponseObject> getById(long id){
        ProductCategory productCategory = productCategoryRepository.findById(id).orElseThrow(()->
                new RuntimeException("Product Category not found"));
        return ResponseEntity.ok(new ResponseObject("get success",productCategoryMapper.toDto(productCategory)));
    }

    public ResponseEntity<ResponseObject> delete(long id){
        Optional<ProductCategory> productCategory = productCategoryRepository.findById(id);
        if(productCategory.isPresent()){
            productCategory.get().setStatus(false);
            productCategoryRepository.save(productCategory.get());
            return ResponseEntity.ok(new ResponseObject("delete success",productCategoryMapper.toDto(productCategory.get())));
        }
        return ResponseEntity.badRequest().body(new ResponseObject("Product category not found",null));
    }

    public ResponseEntity<ResponseObject> update(Long id, ProductCategoryDTO productCategoryDTO){
        ProductCategory existingproductCategory = productCategoryRepository.findById(id).orElseThrow(()->
                new RuntimeException("Product category not found"));
        productCategoryMapper.updateProductCategoryFromDto(productCategoryDTO,existingproductCategory);
        productCategoryRepository.save(existingproductCategory);
        return ResponseEntity.ok(new ResponseObject("update success",productCategoryDTO));
    }
}
