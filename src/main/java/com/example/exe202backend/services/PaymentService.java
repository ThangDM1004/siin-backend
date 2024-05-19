package com.example.exe202backend.services;

import com.example.exe202backend.dto.PaymentDTO;
import com.example.exe202backend.mapper.PaymentMapper;
import com.example.exe202backend.models.Payment;
import com.example.exe202backend.repositories.PaymentRepository;
import com.example.exe202backend.response.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private PaymentMapper paymentMapper;

    public ResponseEntity<ResponseObject> create(PaymentDTO dto) {
        Payment payment = paymentMapper.toEntity(dto);
        paymentRepository.save(payment);
        return ResponseEntity.ok(new ResponseObject("create success",dto));
    }
    public Page<PaymentDTO> getAll(int currentPage, int pageSize, String field){
        Page<Payment> payments = paymentRepository.findAll(
                PageRequest.of(currentPage-1,pageSize, Sort.by(Sort.Direction.ASC,field)));
        return payments.map(paymentMapper::toDto);
    }
    public ResponseEntity<ResponseObject> getById(long id){
        Payment payment = paymentRepository.findById(id).orElseThrow(()->
                new RuntimeException("Payment not found"));
        return ResponseEntity.ok(new ResponseObject("get success",paymentMapper.toDto(payment)));
    }

    public ResponseEntity<ResponseObject> delete(long id){
        Optional<Payment> payment = paymentRepository.findById(id);
        if(payment.isPresent()){
            payment.get().setStatus(false);
            paymentRepository.save(payment.get());
            return ResponseEntity.ok(new ResponseObject("delete success",paymentMapper.toDto(payment.get())));
        }
        return ResponseEntity.badRequest().body(new ResponseObject("Product not found",null));
    }
    public ResponseEntity<ResponseObject> update(Long id,PaymentDTO dto){
        Payment existingPayment = paymentRepository.findById(id).orElseThrow(()
                -> new RuntimeException("Product not found"));
        paymentMapper.updatePaymentFromDto(dto,existingPayment);
        paymentRepository.save(existingPayment);
        return ResponseEntity.ok(new ResponseObject("update success",dto));
    }
}
