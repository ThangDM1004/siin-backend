package com.example.exe202backend.controllers;

import com.example.exe202backend.dto.AccessoryDTO;
import com.example.exe202backend.dto.PageList;
import com.example.exe202backend.response.ResponseObject;
import com.example.exe202backend.services.AccessoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/v1/accessory")
@CrossOrigin(origins = "*")
public class AccessoryController {
    @Autowired
    private AccessoryService accessoryService;

    @GetMapping(value = "/get-all/{currentPage}")
    public ResponseEntity<ResponseObject> getAllAccessory(@PathVariable int currentPage
            , @RequestParam(defaultValue = "5") int pageSize
            , @RequestParam(defaultValue = "name") String field) {
        if(currentPage < 1 || pageSize < 1 || currentPage > pageSize){
            return ResponseEntity.ok(new ResponseObject("get success", accessoryService.get()));
        }
        Page<AccessoryDTO> accessories = accessoryService.getAll(currentPage, pageSize, field);
        var pageList = PageList.<AccessoryDTO>builder()
                .totalPage(accessories.getTotalPages())
                .currentPage(currentPage)
                .listResult(accessories.getContent())
                .build();
        return ResponseEntity.ok(new ResponseObject("get success", pageList));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getAccessoryById(@PathVariable long id) {
        return accessoryService.getById(id);
    }
    @PostMapping
    public ResponseEntity<ResponseObject> createAccessory(@RequestBody AccessoryDTO accessory) {
        return accessoryService.create(accessory);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateAccessory(@PathVariable long id,@RequestBody AccessoryDTO accessory) {
        return accessoryService.update(id,accessory);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteAccessory(@PathVariable long id) {
        return accessoryService.delete(id);
    }

    @PostMapping("/create-image/{accessoryId}")
    public ResponseEntity<ResponseObject> createImage(@PathVariable long accessoryId, @RequestParam("file") MultipartFile file) {
        return accessoryService.createImage(file,accessoryId);
    }

    @PutMapping("/update-image/{accessoryId}")
    public ResponseEntity<ResponseObject> updateImage(@PathVariable long accessoryId, @RequestParam("file") MultipartFile file) throws IOException, URISyntaxException {
        return accessoryService.updateImage(file,accessoryId);
    }

    @DeleteMapping("/delete-image/{accessoryId}")
    public ResponseEntity<ResponseObject> updateImage(@PathVariable long accessoryId) throws IOException, URISyntaxException {
        return accessoryService.deleteImage(accessoryId);
    }
}

