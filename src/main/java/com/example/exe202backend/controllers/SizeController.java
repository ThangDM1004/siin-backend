package com.example.exe202backend.controllers;

import com.example.exe202backend.dto.ColorDTO;
import com.example.exe202backend.dto.PageList;
import com.example.exe202backend.dto.SizeDTO;
import com.example.exe202backend.response.ResponseObject;
import com.example.exe202backend.services.SizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/size")
@CrossOrigin(origins = "*")
public class SizeController {
    @Autowired
    private SizeService sizeService;
    @GetMapping(value = "/get-all/{currentPage}")
    public ResponseEntity<ResponseObject> getAll(@PathVariable int currentPage
            , @RequestParam(defaultValue = "5") int pageSize
            , @RequestParam(defaultValue = "id") String field) {
        if (currentPage < 1 || pageSize < 1 || currentPage > pageSize) {
            return ResponseEntity.ok(new ResponseObject("get success", sizeService.get()));
        }
        Page<SizeDTO> all = sizeService.getAll(currentPage, pageSize, field);
        var pageList = PageList.<SizeDTO>builder()
                .totalPage(all.getTotalPages())
                .currentPage(currentPage)
                .listResult(all.getContent())
                .build();
        return ResponseEntity.ok(new ResponseObject("get success", pageList));
    }

    @PostMapping()
    public ResponseEntity<ResponseObject> add(@RequestBody SizeDTO sizeDTO) {
        return sizeService.create(sizeDTO);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> update(@PathVariable long id, @RequestBody SizeDTO sizeDTO) {
        return sizeService.update(id,sizeDTO);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> delete(@PathVariable long id) {
        return sizeService.delete(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getById(@PathVariable long id) {
        return sizeService.getById(id);
    }
}
