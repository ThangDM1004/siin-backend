package com.example.exe202backend.services;

import com.example.exe202backend.dto.ProductDTO;
import com.example.exe202backend.mapper.ProductMapper;
import com.example.exe202backend.models.Product;
import com.example.exe202backend.repositories.ProductRepository;
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
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductMaterialService productMaterialService;

    public List<ProductDTO> get(){
        return productRepository.findAll().stream().map(productMapper::toDto).collect(Collectors.toList());
    }

    public ResponseEntity<ResponseObject> create(ProductDTO productDTO) {
        Product product = productMapper.toEntity(productDTO);
        productRepository.save(product);
        return ResponseEntity.ok(new ResponseObject("create success",productDTO));
    }
    public Page<ProductDTO> getAll(int currentPage, int pageSize, String field){
        Page<Product> accessories = productRepository.findAll(
                PageRequest.of(currentPage-1,pageSize, Sort.by(Sort.Direction.ASC,field)));
        return accessories.map(productMapper::toDto);
    }
    public ResponseEntity<ResponseObject> getById(long id){
        Product product = productRepository.findById(id).orElseThrow(()->
                new RuntimeException("Product not found"));
        return ResponseEntity.ok(new ResponseObject("get success",productMapper.toDto(product)));
    }

    public ResponseEntity<ResponseObject> delete(long id){
        Optional<Product> product = productRepository.findById(id);
        if(product.isPresent()){
            product.get().setStatus(false);
            productRepository.save(product.get());
            return ResponseEntity.ok(new ResponseObject("delete success",productMapper.toDto(product.get())));
        }
        return ResponseEntity.badRequest().body(new ResponseObject("Product not found",null));
    }
    public ResponseEntity<ResponseObject> update(Long id,ProductDTO productDTO){
        Product existingProduct = productRepository.findById(id).orElseThrow(()
                -> new RuntimeException("Product not found"));
        productMapper.updateProductFromDto(productDTO,existingProduct);
        productRepository.save(existingProduct);
        return ResponseEntity.ok(new ResponseObject("update success",productDTO));
    }

    public Product isExist(long accessoryId, String size, String colorName){
        Long materialId = productMaterialService.getMaterialIdBySizeAndColorName(size,colorName);
        if(materialId == null){ return null;}
        List<Product> allProducts = productRepository.findAll();

        List<Product> filteredProducts = allProducts.stream()
                .filter(product -> product.getAccessory() != null && product.getAccessory().getId() == accessoryId
                        && product.getProductMaterial() != null && product.getProductMaterial().getId() == materialId)
                .toList();
        if (!filteredProducts.isEmpty()) {
            return filteredProducts.get(0);
        }
        return null;
    }
}
