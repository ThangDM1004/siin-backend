package com.example.exe202backend.controllers;

import com.example.exe202backend.dto.PageList;
import com.example.exe202backend.dto.ProductCategoryDTO;
import com.example.exe202backend.response.ResponseObject;
import com.example.exe202backend.services.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/product-category")
public class ProductCategoryController {
    @Autowired
    private ProductCategoryService productCategoryService;

    @GetMapping(value = "/get-all/{currentPage}")
    public ResponseEntity<ResponseObject> getAllAccessory(@PathVariable int currentPage
            , @RequestParam(defaultValue = "5") int pageSize
            , @RequestParam(defaultValue = "id") String field) {
        Page<ProductCategoryDTO> productMaterials = productCategoryService.getAll(currentPage, pageSize, field);
        var pageList = PageList.<ProductCategoryDTO>builder()
                .totalPage(productMaterials.getTotalPages())
                .currentPage(currentPage)
                .listResult(productMaterials.getContent())
                .build();
        return ResponseEntity.ok(new ResponseObject("get success", pageList));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getAccessoryById(@PathVariable long id) {
        return productCategoryService.getById(id);
    }
    @PostMapping
    public ResponseEntity<ResponseObject> createAccessory(@RequestBody ProductCategoryDTO productCategoryDTO) {
        return productCategoryService.create(productCategoryDTO);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateAccessory(@PathVariable long id,@RequestBody ProductCategoryDTO productCategoryDTO) {
        return productCategoryService.update(id,productCategoryDTO);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteAccessory(@PathVariable long id) {
        return productCategoryService.delete(id);
    }
}
