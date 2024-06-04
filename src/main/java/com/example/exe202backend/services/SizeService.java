package com.example.exe202backend.services;

import com.example.exe202backend.dto.ColorDTO;
import com.example.exe202backend.dto.SizeDTO;
import com.example.exe202backend.mapper.SizeMapper;
import com.example.exe202backend.models.Color;
import com.example.exe202backend.models.Size;
import com.example.exe202backend.repositories.SizeRepository;
import com.example.exe202backend.response.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SizeService {
    @Autowired
    private SizeRepository sizeRepository;
    @Autowired
    private SizeMapper sizeMapper;
    public List<SizeDTO> get() {
        return sizeRepository.findAll().stream().map(sizeMapper::toDto).collect(Collectors.toList());
    }

    public Page<SizeDTO> getAll(int currentPage, int pageSize, String field) {
        Page<Size> sizes = sizeRepository.findAll(
                PageRequest.of(currentPage - 1, pageSize, Sort.by(Sort.Direction.ASC, field)));
        return sizes.map(sizeMapper::toDto);
    }

    public ResponseEntity<ResponseObject> getById(Long id) {
        Optional<Size> sizes = sizeRepository.findById(id);
        return sizes.map(value -> ResponseEntity.ok(new ResponseObject("get success", sizeMapper.toDto(value))))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("Not found Size", "")));
    }
    public ResponseEntity<ResponseObject> create(SizeDTO dto) {
        Size size = sizeRepository.save(sizeMapper.toEntity(dto));
        return ResponseEntity.ok(new ResponseObject("create success", sizeMapper.toDto(size)));
    }
    public ResponseEntity<ResponseObject> update(Long id,SizeDTO dto) {
        Optional<Size> sizes = sizeRepository.findById(id);
        if (sizes.isPresent()) {
            if(dto.getName() == null){
                dto.setName(sizes.get().getName());
            }
            if(dto.getPrice() == 0){
                dto.setPrice(sizes.get().getPrice());
            }
            Size updatedSize = sizeRepository.save(sizeMapper.toEntity(dto));
            return ResponseEntity.ok(new ResponseObject("update success", sizeMapper.toDto(updatedSize)));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("Not found Size", ""));
    }
    public ResponseEntity<ResponseObject> delete(Long id) {
        Optional<Size> sizes = sizeRepository.findById(id);
        if (sizes.isPresent()) {
            sizes.get().setStatus(false);
            Size updatedSize = sizeRepository.save(sizes.get());
            return ResponseEntity.ok(new ResponseObject("delete success", sizeMapper.toDto(updatedSize)));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("Not found Size", ""));
    }
}
