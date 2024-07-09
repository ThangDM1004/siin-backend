package com.example.exe202backend.controllers;


import com.example.exe202backend.dto.PageList;
import com.example.exe202backend.dto.ProductDTO;
import com.example.exe202backend.response.ResponseObject;
import com.example.exe202backend.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/v1/product")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping(value = "/get-all/{currentPage}")
    public ResponseEntity<ResponseObject> getAll(
            @RequestParam(defaultValue = "0") int currentPage,
            @RequestParam(defaultValue = "5") int pageSize,
            @RequestParam(defaultValue = "name") String field,
            @RequestParam(required = false, defaultValue = "0") long categoryId) {
         if (categoryId == 0) {
            if (currentPage < 1 || pageSize < 1) {
                return ResponseEntity.ok(new ResponseObject("get success", productService.get()));
            }

            Page<ProductDTO> accessories = productService.getAll(currentPage, pageSize, field);
            var pageList = PageList.<ProductDTO>builder()
                    .totalPage(accessories.getTotalPages())
                    .currentPage(currentPage)
                    .listResult(accessories.getContent())
                    .build();
            return ResponseEntity.ok(new ResponseObject("get success", pageList));
        }else{
            if (currentPage < 1 || pageSize < 1) {
                return ResponseEntity.ok(new ResponseObject("get success", productService.getByCategory(categoryId)));
            }

            Page<ProductDTO> accessories = productService.searchByCategory(currentPage, pageSize, field,categoryId);
            var pageList = PageList.<ProductDTO>builder()
                    .totalPage(accessories.getTotalPages())
                    .currentPage(currentPage)
                    .listResult(accessories.getContent())
                    .build();
            return ResponseEntity.ok(new ResponseObject("get success", pageList));
        }

    }
    @GetMapping(value = "/get-all-excluding-customize/{currentPage}")
    public ResponseEntity<ResponseObject> getAllExcludingCustomize(
            @RequestParam(defaultValue = "0") int currentPage,
            @RequestParam(defaultValue = "5") int pageSize,
            @RequestParam(defaultValue = "name") String field,
            @RequestParam(required = false, defaultValue = "0") long categoryId) {
        if (categoryId == 0) {
            if (currentPage < 1 || pageSize < 1) {
                return ResponseEntity.ok(new ResponseObject("get success",
                        productService.getAllExcludingCustomize()));
            }
//
            Page<ProductDTO> accessories = productService.getAllExcludingCustomize(currentPage, pageSize, field);
            var pageList = PageList.<ProductDTO>builder()
                    .totalPage(accessories.getTotalPages())
                    .currentPage(currentPage)
                    .listResult(accessories.getContent())
                    .build();
            return ResponseEntity.ok(new ResponseObject("get success", pageList));
        }else{
            if (currentPage < 1 || pageSize < 1 || currentPage > pageSize) {
                return ResponseEntity.ok(new ResponseObject("get success", productService
                        .getByCategory(categoryId)));
            }
            Page<ProductDTO> accessories = productService.searchByCategoryIdExcludingCustomize(currentPage, pageSize, field,categoryId);
            var pageList = PageList.<ProductDTO>builder()
                    .totalPage(accessories.getTotalPages())
                    .currentPage(currentPage)
                    .listResult(accessories.getContent())
                    .build();
            return ResponseEntity.ok(new ResponseObject("get success", pageList));
        }

    }
    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getById(@PathVariable long id) {
        return productService.getById(id);
    }

    @PostMapping
    public ResponseEntity<ResponseObject> create(@RequestBody ProductDTO productDTO) {
        return productService.create(productDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> update(@PathVariable long id, @RequestBody ProductDTO productDTO) {
        return productService.update(id, productDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> delete(@PathVariable long id) {
        return productService.delete(id);
    }

    @PostMapping("/upload-image/{productId}")
    public ResponseEntity<ResponseObject> uploadImage(@RequestParam("file") MultipartFile multipartFile,
                                                      @PathVariable Long productId) {
        return productService.createCoverImage(multipartFile, productId);
    }

    @PutMapping("/update-image/{productId}")
    public ResponseEntity<ResponseObject> updateImage(@RequestParam("file") MultipartFile multipartFile,
                                                      @PathVariable Long productId) throws IOException, URISyntaxException {
        return productService.updateCoverImage(multipartFile, productId);
    }

    @DeleteMapping("/delete-image/{productId}")
    public ResponseEntity<ResponseObject> uploadImage(@PathVariable Long productId) throws IOException, URISyntaxException {
        return productService.deleteCoverImage(productId);
    }
}
