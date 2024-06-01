package com.example.exe202backend.services;

import com.example.exe202backend.dto.UserAddressDTO;
import com.example.exe202backend.dto.UserDTO;
import com.example.exe202backend.mapper.UserMapper;
import com.example.exe202backend.models.UserAddress;
import com.example.exe202backend.models.UserModel;
import com.example.exe202backend.repositories.UserRepository;
import com.example.exe202backend.response.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toDto).collect(Collectors.toList());
    }
    public Page<UserDTO> getAll(int currentPage, int pageSize, String field){
        Page<UserModel> userModels = userRepository.findAll(
                PageRequest.of(currentPage-1,pageSize, Sort.by(Sort.Direction.ASC,field)));
        return userModels.map(userMapper::toDto);
    }
    public ResponseEntity<ResponseObject> getUserById(Long id) {
        UserModel user = userRepository.findById(id).orElseThrow(()->
                new RuntimeException("User not found"));
        return ResponseEntity.ok(new ResponseObject("get success",userMapper.toDto(user)));
    }
//    public ResponseEntity<ResponseObject> update(Long id,UserDTO userDTO) {
//        UserModel userModel = userRepository.findById(id).orElseThrow(()->
//                new RuntimeException("User not found"));
//        if(userDTO.getAvatar() == null){
//            userDTO.setAvatar(userModel.getAvatar());
//        }
//
//    }
}
