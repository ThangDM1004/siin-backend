package com.example.exe202backend.controllers;

import com.example.exe202backend.dto.CreateProductMaterialDTO;
import com.example.exe202backend.dto.PageList;
import com.example.exe202backend.dto.ProductMaterialDTO;
import com.example.exe202backend.response.ResponseObject;
import com.example.exe202backend.services.ProductMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/product-material")
@CrossOrigin(origins = "*")
public class ProductMaterialController {
    @Autowired
    private ProductMaterialService productMaterialService;
    @GetMapping(value = "/get-all/{currentPage}")
    public ResponseEntity<ResponseObject> getAll(@PathVariable int currentPage
            , @RequestParam(defaultValue = "5") int pageSize
            , @RequestParam(defaultValue = "id") String field) {
        if(currentPage < 1 || pageSize < 1 || currentPage > pageSize){
            return ResponseEntity.ok(new ResponseObject("get success", productMaterialService.get()));
        }
        Page<ProductMaterialDTO> productMaterials = productMaterialService.getAll(currentPage, pageSize, field);
        var pageList = PageList.<ProductMaterialDTO>builder()
                .totalPage(productMaterials.getTotalPages())
                .currentPage(currentPage)
                .listResult(productMaterials.getContent())
                .build();
        return ResponseEntity.ok(new ResponseObject("get success", pageList));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getById(@PathVariable long id) {
        return productMaterialService.getById(id);
    }
    @PostMapping
    public ResponseEntity<ResponseObject> create(@RequestBody CreateProductMaterialDTO productMaterialDTO, @RequestParam List<Long> listColor,@RequestParam List<Long> listSize) {
        return productMaterialService.create(productMaterialDTO,listColor,listSize);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> update(@PathVariable long id,@RequestBody ProductMaterialDTO productMaterialDTO) {
        return productMaterialService.update(id,productMaterialDTO);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> delete(@PathVariable long id) {
        return productMaterialService.delete(id);
    }

    @PostMapping("/create-image/{productMaterialId}")
    public ResponseEntity<ResponseObject> createImage(@PathVariable long productMaterialId, @RequestParam("file") MultipartFile file) {
        return productMaterialService.createImage(file,productMaterialId);
    }

    @PutMapping("/update-image/{productMaterialId}")
    public ResponseEntity<ResponseObject> updateImage(@PathVariable long productMaterialId, @RequestParam("file") MultipartFile file) throws IOException, URISyntaxException {
        return productMaterialService.updateImage(file,productMaterialId);
    }

    @DeleteMapping("/delete-image/{productMaterialId}")
    public ResponseEntity<ResponseObject> deleteImage(@PathVariable long productMaterialId) throws IOException, URISyntaxException {
        return productMaterialService.deleteImage(productMaterialId);
    }

    @GetMapping("/get-by-productId/{productId}")
    public ResponseEntity<ResponseObject> getByProductId(@PathVariable Long productId){
        return productMaterialService.getProductMaterialByProductId(productId);
    }
}
