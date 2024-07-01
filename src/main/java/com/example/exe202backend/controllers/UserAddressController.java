package com.example.exe202backend.controllers;

import com.example.exe202backend.dto.PageList;
import com.example.exe202backend.dto.UserAddressDTO;
import com.example.exe202backend.response.ResponseObject;
import com.example.exe202backend.services.UserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user-address")
@CrossOrigin(origins = "*")
public class UserAddressController {
    @Autowired
    private UserAddressService userAddressService;
    @GetMapping(value = "/get-all/{currentPage}")
    public ResponseEntity<ResponseObject> getALl(@PathVariable int currentPage
            , @RequestParam(defaultValue = "5") int pageSize
            , @RequestParam(defaultValue = "id") String field) {
        if(currentPage < 1 || pageSize < 1){
            return ResponseEntity.ok(new ResponseObject("get success", userAddressService.get()));
        }
        Page<UserAddressDTO> all = userAddressService.getAll(currentPage, pageSize, field);
        var pageList = PageList.<UserAddressDTO>builder()
                .totalPage(all.getTotalPages())
                .currentPage(currentPage)
                .listResult(all.getContent())
                .build();
        return ResponseEntity.ok(new ResponseObject("get success", pageList));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getById(@PathVariable long id) {
        return userAddressService.getById(id);
    }
    @PostMapping
    public ResponseEntity<ResponseObject> create(@RequestBody UserAddressDTO dto) {
        return userAddressService.create(dto);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> update(@PathVariable long id,@RequestBody UserAddressDTO dto) {
        return userAddressService.update(id,dto);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> delete(@PathVariable long id) {
        return userAddressService.delete(id);
    }
}
