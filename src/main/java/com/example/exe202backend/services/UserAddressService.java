package com.example.exe202backend.services;

import com.example.exe202backend.dto.UserAddressDTO;
import com.example.exe202backend.mapper.UserAddressMapper;
import com.example.exe202backend.models.UserAddress;
import com.example.exe202backend.repositories.UserAddressRepository;
import com.example.exe202backend.repositories.UserRepository;
import com.example.exe202backend.response.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserAddressService {
    @Autowired
    private UserAddressRepository userAddressRepository;
    @Autowired
    private UserAddressMapper userAddressMapper;
    @Autowired
    private UserRepository userRepository;

    public List<UserAddressDTO> get(){
        return userAddressRepository.findAll().stream().map(userAddressMapper::toDto).collect(Collectors.toList());
    }

    public ResponseEntity<ResponseObject> create(UserAddressDTO dto) {
        UserAddress address = userAddressMapper.toEntity(dto);
        address.setUserModel(userRepository.findById(dto.getId()).orElse(null));
        userAddressRepository.save(address);
        return ResponseEntity.ok(new ResponseObject("create success",dto));
    }
    public Page<UserAddressDTO> getAll(int currentPage, int pageSize, String field){
        Page<UserAddress> addresses = userAddressRepository.findAll(
                PageRequest.of(currentPage-1,pageSize, Sort.by(Sort.Direction.ASC,field)));
        return addresses.map(userAddressMapper::toDto);
    }
    public ResponseEntity<ResponseObject> getById(long id){
        UserAddress address = userAddressRepository.findById(id).orElseThrow(()->
                new RuntimeException("User address not found"));
        return ResponseEntity.ok(new ResponseObject("get success",userAddressMapper.toDto(address)));
    }

    public ResponseEntity<ResponseObject> delete(long id){
        Optional<UserAddress> address = userAddressRepository.findById(id);
        if(address.isPresent()){
            address.get().setStatus(false);
            userAddressRepository.save(address.get());
            return ResponseEntity.ok(new ResponseObject("delete success",userAddressMapper.toDto(address.get())));
        }
        return ResponseEntity.badRequest().body(new ResponseObject("Product not found",null));
    }
    public ResponseEntity<ResponseObject> update(Long id,UserAddressDTO dto){
        UserAddress address = userAddressRepository.findById(id).orElseThrow(()->
                new RuntimeException("Product Sub Image not found"));
        if(dto.getUserId() == 0){
            dto.setUserId(address.getUserModel().getId());
        }
        if(dto.getAddress() == null){
            dto.setAddress(address.getAddress());
        }
        if(dto.getWard() == null){
            dto.setWard(address.getWard());
        }
        if(dto.getProvince() == null){
            dto.setProvince(address.getProvince());
        }
        if(dto.getDistrict() == null){
            dto.setDistrict(address.getDistrict());
        }
        userAddressMapper.updateUserAddressFromDto(dto,address);
        address.setUserModel(userRepository.findById(dto.getId()).orElse(null));
        userAddressRepository.save(address);
        return ResponseEntity.ok(new ResponseObject("update success",dto));
    }
}
