package com.example.exe202backend.controllers;

import com.example.exe202backend.dto.PageList;
import com.example.exe202backend.dto.PaymentDTO;
import com.example.exe202backend.response.ResponseObject;
import com.example.exe202backend.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payment")
@CrossOrigin(origins = "*")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @GetMapping(value = "/get-all/{currentPage}")
    public ResponseEntity<ResponseObject> getAll(@PathVariable int currentPage
            , @RequestParam(defaultValue = "5") int pageSize
            , @RequestParam(defaultValue = "id") String field) {
        if(currentPage < 1 || pageSize < 1 || currentPage > pageSize){
            return ResponseEntity.ok(new ResponseObject("get success", paymentService.get()));
        }
        Page<PaymentDTO> paymentDTOS = paymentService.getAll(currentPage, pageSize, field);
        var pageList = PageList.<PaymentDTO>builder()
                .totalPage(paymentDTOS.getTotalPages())
                .currentPage(currentPage)
                .listResult(paymentDTOS.getContent())
                .build();
        return ResponseEntity.ok(new ResponseObject("get success", pageList));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getById(@PathVariable long id) {
        return paymentService.getById(id);
    }
    @PostMapping
    public ResponseEntity<ResponseObject> create(@RequestBody PaymentDTO dto) {
        return paymentService.create(dto);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> update(@PathVariable long id,@RequestBody PaymentDTO dto) {
        return paymentService.update(id,dto);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> delete(@PathVariable long id) {
        return paymentService.delete(id);
    }
}
