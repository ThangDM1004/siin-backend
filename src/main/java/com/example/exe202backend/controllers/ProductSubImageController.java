package com.example.exe202backend.controllers;

import com.example.exe202backend.dto.CartDTO;
import com.example.exe202backend.dto.PageList;
import com.example.exe202backend.dto.ProductSubImageDTO;
import com.example.exe202backend.response.ResponseObject;
import com.example.exe202backend.services.ProductSubImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/product-sub-image")
public class ProductSubImageController {
    @Autowired
    private ProductSubImageService productSubImageService;
    @GetMapping(value = "/get-all/{currentPage}")
    public ResponseEntity<ResponseObject> getAllAccessory(@PathVariable int currentPage
            , @RequestParam(defaultValue = "5") int pageSize
            , @RequestParam(defaultValue = "id") String field) {
        Page<ProductSubImageDTO> accessories = productSubImageService.getAll(currentPage, pageSize, field);
        var pageList = PageList.<ProductSubImageDTO>builder()
                .totalPage(accessories.getTotalPages())
                .currentPage(currentPage)
                .listResult(accessories.getContent())
                .build();
        return ResponseEntity.ok(new ResponseObject("get success", pageList));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getAccessoryById(@PathVariable long id) {
        return productSubImageService.getById(id);
    }
    @PostMapping
    public ResponseEntity<ResponseObject> createAccessory(@RequestBody ProductSubImageDTO productSubImageDTO) {
        return productSubImageService.create(productSubImageDTO);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateAccessory(@PathVariable long id,@RequestBody ProductSubImageDTO productSubImageDTO) {
        return productSubImageService.update(id,productSubImageDTO);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteAccessory(@PathVariable long id) {
        return productSubImageService.delete(id);
    }
}
