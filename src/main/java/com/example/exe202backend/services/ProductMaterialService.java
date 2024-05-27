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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductMaterialService {
    @Autowired
    private ProductMaterialRepository productMaterialRepository;
    @Autowired
    private ProductMaterialMapper productMaterialMapper;

    public List<ProductMaterialDTO> get(){
        return productMaterialRepository.findAll().stream().map(productMaterialMapper::toDto).collect(Collectors.toList());
    }

    public ResponseEntity<ResponseObject> create(ProductMaterialDTO productMaterialDTO){
        ProductMaterial productMaterial = productMaterialMapper.toEntity(productMaterialDTO);
        productMaterialRepository.save(productMaterial);
        return ResponseEntity.ok(new ResponseObject("create success", productMaterialDTO));
    }
    public Page<ProductMaterialDTO> getAll(int currentPage, int pageSize, String field){
        Page<ProductMaterial> productMaterials = productMaterialRepository.findAll(
                PageRequest.of(currentPage-1,pageSize, Sort.by(Sort.Direction.ASC,field)));
        return productMaterials.map(productMaterialMapper::toDto);
    }

    public ResponseEntity<ResponseObject> getById(long id){
        ProductMaterial productMaterial = productMaterialRepository.findById(id).orElseThrow(()
                -> new RuntimeException("Product material not found"));
        return ResponseEntity.ok(new ResponseObject("get success",productMaterialMapper.toDto(productMaterial)));
    }

    public ResponseEntity<ResponseObject> delete(long id){
        Optional<ProductMaterial> productMaterial = productMaterialRepository.findById(id);
        if(productMaterial.isPresent()){
            productMaterial.get().setStatus(false);
            productMaterialRepository.save(productMaterial.get());
            return ResponseEntity.ok(new ResponseObject("delete success",
                    productMaterialMapper.toDto(productMaterial.get())));
        }
        return ResponseEntity.badRequest().body(new ResponseObject("delete fail",null));
    }

    public ResponseEntity<ResponseObject> update(Long id, ProductMaterialDTO productMaterialDTO){
        ProductMaterial existingProductMaterial = productMaterialRepository.findById(id).orElseThrow(()
                -> new RuntimeException("Product material not found"));
        productMaterialMapper.updateProductMaterialFromDto(productMaterialDTO,existingProductMaterial);
        productMaterialRepository.save(existingProductMaterial);
        return ResponseEntity.ok(new ResponseObject("update success",productMaterialDTO));
    }
}
