package com.example.exe202backend.controllers;

import com.example.exe202backend.dto.PageList;
import com.example.exe202backend.dto.ProductSubImageDTO;
import com.example.exe202backend.response.ResponseObject;
import com.example.exe202backend.services.ProductSubImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/product-sub-image")
@CrossOrigin(origins = "*")
public class ProductSubImageController {
    @Autowired
    private ProductSubImageService productSubImageService;
    @GetMapping(value = "/get-all/{currentPage}")
    public ResponseEntity<ResponseObject> getAll(@PathVariable int currentPage
            , @RequestParam(defaultValue = "5") int pageSize
            , @RequestParam(defaultValue = "id") String field) {
        if(currentPage < 1 || pageSize < 1 || currentPage > pageSize){
            return ResponseEntity.ok(new ResponseObject("get success", productSubImageService.get()));
        }
        Page<ProductSubImageDTO> accessories = productSubImageService.getAll(currentPage, pageSize, field);
        var pageList = PageList.<ProductSubImageDTO>builder()
                .totalPage(accessories.getTotalPages())
                .currentPage(currentPage)
                .listResult(accessories.getContent())
                .build();
        return ResponseEntity.ok(new ResponseObject("get success", pageList));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getById(@PathVariable long id) {
        return productSubImageService.getById(id);
    }
    @PostMapping("/{productMaterialId}")
    public ResponseEntity<ResponseObject> create(@RequestParam("file") List<MultipartFile> multipartFile,@PathVariable Long productMaterialId) {
        return productSubImageService.createSubImage(multipartFile, productMaterialId);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> update(@PathVariable long id,@RequestParam("file") MultipartFile multipartFile) throws IOException, URISyntaxException {
        return productSubImageService.updateImage(multipartFile,id);
    }
    @GetMapping("/productMaterialId")
    public ResponseEntity<ResponseObject> getByProductId(@RequestParam long productMaterialId) {
       return productSubImageService.getByProductMaterialId(productMaterialId);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteImage(@RequestParam long id) throws IOException, URISyntaxException {
        return productSubImageService.deleteImage(id);
    }
}
