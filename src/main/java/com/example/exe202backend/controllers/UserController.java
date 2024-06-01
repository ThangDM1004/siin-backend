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
}
