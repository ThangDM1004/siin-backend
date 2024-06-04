package com.example.exe202backend.services;

import com.example.exe202backend.dto.ColorDTO;
import com.example.exe202backend.dto.ProductDTO;
import com.example.exe202backend.mapper.ColorMapper;
import com.example.exe202backend.models.Color;
import com.example.exe202backend.models.Product;
import com.example.exe202backend.repositories.ColorRepository;
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
public class ColorService {
    @Autowired
    private ColorRepository colorRepository;
    @Autowired
    private ColorMapper colorMapper;

    public List<ColorDTO> get() {
       return colorRepository.findAll().stream().map(colorMapper::toDto).collect(Collectors.toList());
    }

    public Page<ColorDTO> getAll(int currentPage, int pageSize, String field) {
        Page<Color> colors = colorRepository.findAll(
                PageRequest.of(currentPage - 1, pageSize, Sort.by(Sort.Direction.ASC, field)));
        return colors.map(colorMapper::toDto);
    }

    public ResponseEntity<ResponseObject> getById(long id) {
        Optional<Color> color = colorRepository.findById(id);
        return color.map(value -> ResponseEntity.ok(new ResponseObject("get success", colorMapper.toDto(value))))
                .orElseGet(() -> ResponseEntity.ok(new ResponseObject("get success", null)));
    }

    public ResponseEntity<ResponseObject> delete(long id) {
        Optional<Color> color = colorRepository.findById(id);
        if (color.isPresent()) {
            color.get().setStatus(false);
            colorRepository.save(color.get());
            return ResponseEntity.ok(new ResponseObject("delete success", colorMapper.toDto(color.get())));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("Color not found", ""));
    }

    public ResponseEntity<ResponseObject> update(long id, ColorDTO colorDTO) {
        Optional<Color> color = colorRepository.findById(id);
        if (color.isPresent()) {
            if(colorDTO.getName() == null){
                colorDTO.setName(color.get().getName());
            }
            if(colorDTO.getPrice() == 0){
                colorDTO.setPrice(color.get().getPrice());
            }
            Color savedColor = colorRepository.save(color.get());
            return ResponseEntity.ok(new ResponseObject("update success", colorMapper.toDto(savedColor)));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("Color not found", ""));
    }

    public ResponseEntity<ResponseObject> create(ColorDTO colorDTO){
        Color savedColor = colorRepository.save(colorMapper.toEntity(colorDTO));
        return ResponseEntity.ok(new ResponseObject("create success", colorMapper.toDto(savedColor)));
    }
}
