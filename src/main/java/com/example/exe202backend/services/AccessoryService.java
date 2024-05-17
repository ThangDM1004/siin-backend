package com.example.exe202backend.services;


import com.example.exe202backend.dto.AccessoryDTO;
import com.example.exe202backend.mapper.AccessoryMapper;
import com.example.exe202backend.models.Accessory;
import com.example.exe202backend.repositories.AccessoryRepository;
import com.example.exe202backend.response.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccessoryService {

    @Autowired
    private AccessoryRepository accessoryRepository;
    @Autowired
    private AccessoryMapper accessoryMapper;

    public ResponseEntity<ResponseObject> create(AccessoryDTO accessoryDto) {
        Accessory accessory = accessoryMapper.toEntity(accessoryDto);
        accessoryRepository.save(accessory);
        return ResponseEntity.ok(new ResponseObject("create success",accessory));
    }

    public Page<Accessory>  getAll(int currentPage, int pageSize, String field){
        return accessoryRepository.findAll(
                PageRequest.of(currentPage-1,pageSize, Sort.by(Sort.Direction.ASC,field)));
    }

    public ResponseEntity<ResponseObject> getById(long id){
        Optional<Accessory> accessory = accessoryRepository.findById(id);
        return ResponseEntity.ok(new ResponseObject("get success",accessory));
    }

    public ResponseEntity<ResponseObject> delete(long id){
        Optional<Accessory> accessory = accessoryRepository.findById(id);
        if(accessory.isPresent()){
            accessoryRepository.delete(accessory.get());
            return ResponseEntity.ok(new ResponseObject("get success",accessory.get()));
        }
        return ResponseEntity.badRequest().body(new ResponseObject("get success",null));
    }

    public ResponseEntity<ResponseObject> update(Long id,AccessoryDTO accessoryDto){
        Accessory existingAccessory = accessoryRepository.findById(id).orElseThrow(()
                -> new RuntimeException("Accessory not found"));
        accessoryMapper.updateAccessoryFromDto(accessoryDto,existingAccessory);
        Accessory updatedAccessory = accessoryRepository.save(existingAccessory);
        return ResponseEntity.ok(new ResponseObject("update success",updatedAccessory));
    }

}

