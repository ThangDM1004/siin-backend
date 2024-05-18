package com.example.exe202backend.controllers;

import com.example.exe202backend.dto.PageList;
import com.example.exe202backend.dto.ProductMaterialDTO;
import com.example.exe202backend.models.ProductMaterial;
import com.example.exe202backend.response.ResponseObject;
import com.example.exe202backend.services.ProductMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/product-material")
public class ProductMaterialController {
    @Autowired
    private ProductMaterialService productMaterialService;
    @GetMapping(value = "/get-all/{currentPage}")
    public ResponseEntity<ResponseObject> getAllAccessory(@PathVariable int currentPage
            , @RequestParam(defaultValue = "5") int pageSize
            , @RequestParam(defaultValue = "id") String field) {
        Page<ProductMaterialDTO> productMaterials = productMaterialService.getAll(currentPage, pageSize, field);
        var pageList = PageList.<ProductMaterialDTO>builder()
                .totalPage(productMaterials.getTotalPages())
                .currentPage(currentPage)
                .listResult(productMaterials.getContent())
                .build();
        return ResponseEntity.ok(new ResponseObject("get success", pageList));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getAccessoryById(@PathVariable long id) {
        return productMaterialService.getById(id);
    }
    @PostMapping
    public ResponseEntity<ResponseObject> createAccessory(@RequestBody ProductMaterialDTO productMaterialDTO) {
        return productMaterialService.create(productMaterialDTO);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateAccessory(@PathVariable long id,@RequestBody ProductMaterialDTO productMaterialDTO) {
        return productMaterialService.update(id,productMaterialDTO);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteAccessory(@PathVariable long id) {
        return productMaterialService.delete(id);
    }
}
