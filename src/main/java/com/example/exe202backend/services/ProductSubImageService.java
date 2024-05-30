package com.example.exe202backend.services;

import com.example.exe202backend.dto.ProductSubImageDTO;
import com.example.exe202backend.mapper.ProductSubImageMapper;
import com.example.exe202backend.models.ProductSubImage;
import com.example.exe202backend.repositories.ProductSubImageRepository;
import com.example.exe202backend.response.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductSubImageService {
    @Autowired
    private ProductSubImageRepository productSubImageRepository;
    @Autowired
    private ProductSubImageMapper productSubImageMapper;

    public List<ProductSubImageDTO> get(){
        return productSubImageRepository.findAll().stream().map(productSubImageMapper::toDto).collect(Collectors.toList());
    }

    public ResponseEntity<ResponseObject> create(ProductSubImageDTO productSubImageDTO) {
        ProductSubImage productSubImage = productSubImageMapper.toEntity(productSubImageDTO);
        productSubImageRepository.save(productSubImage);
        return ResponseEntity.ok(new ResponseObject("create success",productSubImageDTO));
    }
    public Page<ProductSubImageDTO> getAll(int currentPage, int pageSize, String field){
        Page<ProductSubImage> productSubImages = productSubImageRepository.findAll(
                PageRequest.of(currentPage-1,pageSize, Sort.by(Sort.Direction.ASC,field)));
        return productSubImages.map(productSubImageMapper::toDto);
    }
    public ResponseEntity<ResponseObject> getById(long id){
        ProductSubImage productSubImage = productSubImageRepository.findById(id).orElseThrow(()->
                new RuntimeException("Product Sub Image not found"));
        return ResponseEntity.ok(new ResponseObject("get success",productSubImageMapper.toDto(productSubImage)));
    }

    public ResponseEntity<ResponseObject> delete(long id){
        Optional<ProductSubImage> productSubImage = productSubImageRepository.findById(id);
        if(productSubImage.isPresent()){
            productSubImage.get().setStatus(false);
            productSubImageRepository.save(productSubImage.get());
            return ResponseEntity.ok(new ResponseObject("delete success",productSubImageMapper.toDto(productSubImage.get())));
        }
        return ResponseEntity.badRequest().body(new ResponseObject("Product not found",null));
    }
    public ResponseEntity<ResponseObject> update(Long id,ProductSubImageDTO productSubImageDTO){
        ProductSubImage productSubImage = productSubImageRepository.findById(id).orElseThrow(()->
                new RuntimeException("Product Sub Image not found"));
        productSubImageMapper.updateProductSubImageFromDto(productSubImageDTO,productSubImage);
        productSubImageRepository.save(productSubImage);
        return ResponseEntity.ok(new ResponseObject("update success",productSubImageDTO));
    }

    public ResponseEntity<ResponseObject> getByProductId(Long productId){
        ArrayList<ProductSubImage> list = productSubImageRepository.findProductSubImageByProduct_Id(productId);

        if(list.isEmpty()){
            return ResponseEntity.ok(new ResponseObject("Product don't have image",""));
        }else{
            List<ProductSubImageDTO> _list = list.stream().map(productSubImageMapper::toDto).toList();
            return ResponseEntity.ok(new ResponseObject("Get list image success",_list));
        }
    }
}
