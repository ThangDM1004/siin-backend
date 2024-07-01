package com.example.exe202backend.controllers;

import com.example.exe202backend.dto.CartItemResponseDTO;
import com.example.exe202backend.dto.ColorDTO;
import com.example.exe202backend.dto.PageList;
import com.example.exe202backend.response.ResponseObject;
import com.example.exe202backend.services.ColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/color")
@CrossOrigin(origins = "*")
public class ColorController {
    @Autowired
    private ColorService colorService;

    @GetMapping(value = "/get-all/{currentPage}")
    public ResponseEntity<ResponseObject> getAllColors(@PathVariable int currentPage
            , @RequestParam(defaultValue = "5") int pageSize
            , @RequestParam(defaultValue = "id") String field) {
        if (currentPage < 1 || pageSize < 1) {
            return ResponseEntity.ok(new ResponseObject("get success", colorService.get()));
        }
        Page<ColorDTO> all = colorService.getAll(currentPage, pageSize, field);
        var pageList = PageList.<ColorDTO>builder()
                .totalPage(all.getTotalPages())
                .currentPage(currentPage)
                .listResult(all.getContent())
                .build();
        return ResponseEntity.ok(new ResponseObject("get success", pageList));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getColorById(@PathVariable int id) {
        return colorService.getById(id);
    }

    @PostMapping("")
    public ResponseEntity<ResponseObject> addColor(@RequestBody ColorDTO colorDTO) {
        return colorService.create(colorDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateColor(@PathVariable int id, @RequestBody ColorDTO colorDTO) {
        return colorService.update(id, colorDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteColor(@PathVariable int id) {
        return colorService.delete(id);
    }
}

