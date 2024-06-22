package com.example.exe202backend.controllers;

import com.example.exe202backend.dto.PageList;
import com.example.exe202backend.dto.UserAddressDTO;
import com.example.exe202backend.dto.UserDTO;
import com.example.exe202backend.response.ResponseObject;
import com.example.exe202backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/v1/user")
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    private UserService service;
    @GetMapping(value = "/get-all/{currentPage}")
    public ResponseEntity<ResponseObject> getALl(@PathVariable int currentPage
            , @RequestParam(defaultValue = "5") int pageSize
            , @RequestParam(defaultValue = "id") String field) {
        if(currentPage < 1 || pageSize < 1 || currentPage > pageSize){
            return ResponseEntity.ok(new ResponseObject("get success", service.getAllUsers()));
        }
        Page<UserDTO> all = service.getAll(currentPage, pageSize, field);
        var pageList = PageList.<UserDTO>builder()
                .totalPage(all.getTotalPages())
                .currentPage(currentPage)
                .listResult(all.getContent())
                .build();
        return ResponseEntity.ok(new ResponseObject("get success", pageList));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getById(@PathVariable long id) {
        return service.getUserById(id);
    }
    @GetMapping("/getUserIdByToken")
    public ResponseEntity<ResponseObject> getUserIdByToken(@RequestParam String token){
        return ResponseEntity.ok(new ResponseObject("get success", service.getUserIdByToken(token)));
    }
    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> update(@PathVariable long id, @RequestBody UserDTO userDTO) {
        return service.update(id, userDTO);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> delete(@PathVariable long id) {
        return service.delete(id);
    }
    @PostMapping("/create-avatar/{userId}")
    public ResponseEntity<ResponseObject> createImage(@PathVariable long userId, @RequestParam("file") MultipartFile file) {
        return service.createAvartar(file,userId);
    }

    @PutMapping("/update-avatar/{userId}")
    public ResponseEntity<ResponseObject> updateAvatar(@PathVariable long userId, @RequestParam("file") MultipartFile file) throws IOException, URISyntaxException {
        return service.updateAvartar(file,userId);
    }

    @DeleteMapping("/delete-avatar/{userId}")
    public ResponseEntity<ResponseObject> deleteAvatar(@PathVariable long userId) throws IOException, URISyntaxException {
        return service.deleteAvartar(userId);
    }
}
