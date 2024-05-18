package com.example.exe202backend.services;

import com.example.exe202backend.dto.ProductMaterialDTO;
import com.example.exe202backend.mapper.ProductMaterialMapper;
import com.example.exe202backend.models.ProductMaterial;
import com.example.exe202backend.repositories.ProductMaterialRepository;
import com.example.exe202backend.response.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductMaterialService {
    @Autowired
    private ProductMaterialRepository productMaterialRepository;
    @Autowired
    private ProductMaterialMapper productMaterialMapper;

    public ResponseEntity<ResponseObject> create(ProductMaterialDTO productMaterialDTO){
        ProductMaterial productMaterial = productMaterialMapper.toEntity(productMaterialDTO);
        productMaterialRepository.save(productMaterial);
        return ResponseEntity.ok(new ResponseObject("create success", productMaterial));
    }
    public Page<ProductMaterial> getAll(int currentPage, int pageSize, String field){
        return productMaterialRepository.findAll(
                PageRequest.of(currentPage-1,pageSize, Sort.by(Sort.Direction.ASC,field)));
    }

    public ResponseEntity<ResponseObject> getById(long id){
        Optional<ProductMaterial> productMaterial = productMaterialRepository.findById(id);
        return ResponseEntity.ok(new ResponseObject("get success",productMaterial));
    }

    public ResponseEntity<ResponseObject> delete(long id){
        Optional<ProductMaterial> productMaterial = productMaterialRepository.findById(id);
        if(productMaterial.isPresent()){
            productMaterial.get().setStatus(false);
            productMaterialRepository.save(productMaterial.get());
            return ResponseEntity.ok(new ResponseObject("delete success",productMaterial.get()));
        }
        return ResponseEntity.badRequest().body(new ResponseObject("delete fail",null));
    }

    public ResponseEntity<ResponseObject> update(Long id, ProductMaterialDTO productMaterialDTO){
        ProductMaterial existingProductMaterial = productMaterialRepository.findById(id).orElseThrow(()
                -> new RuntimeException("Accessory not found"));
        productMaterialMapper.updateProductMaterialFromDto(productMaterialDTO,existingProductMaterial);
        ProductMaterial updatedProductMaterial = productMaterialRepository.save(existingProductMaterial);
        return ResponseEntity.ok(new ResponseObject("update success",updatedProductMaterial));
    }
}
