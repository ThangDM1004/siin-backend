package com.example.exe202backend.services;

import com.example.exe202backend.dto.UserAddressDTO;
import com.example.exe202backend.dto.UserDTO;
import com.example.exe202backend.mapper.UserMapper;
import com.example.exe202backend.models.Product;
import com.example.exe202backend.models.UserAddress;
import com.example.exe202backend.models.UserModel;
import com.example.exe202backend.repositories.UserRepository;
import com.example.exe202backend.response.ResponseObject;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
    public ResponseEntity<ResponseObject> update(Long id,UserDTO userDTO) {
        UserModel userModel = userRepository.findById(id).orElseThrow(()->
                new RuntimeException("User not found"));
        if(userDTO.getAvatar() == null){
            userDTO.setAvatar(userModel.getAvatar());
        }
        if(userDTO.getDob() == null){
            userDTO.setDob(userModel.getDob());
        }
        if(userDTO.getEmail() == null){
            userDTO.setEmail(userModel.getEmail());
        }
        if(userDTO.getPhone() == null){
            userDTO.setPhone(userModel.getPhone());
        }
        if(userDTO.getFullName() == null){
            userDTO.setFullName(userModel.getFullName());
        }
        userMapper.updateUserFromDto(userDTO,userModel);
        userRepository.save(userModel);
        return ResponseEntity.ok(new ResponseObject("update success",userMapper.toDto(userModel)));
    }

    public ResponseEntity<ResponseObject> delete(Long id) {
        UserModel userModel = userRepository.findById(id).orElseThrow(()->
                new RuntimeException("User not found"));
        userModel.setStatus(false);
        userRepository.save(userModel);
        return ResponseEntity.ok(new ResponseObject("delete success",userMapper.toDto(userModel)));
    }

    public ResponseEntity<ResponseObject> createAvartar(MultipartFile multipartFile, Long id) {
        Optional<UserModel> userModel = userRepository.findById(id);
        if (userModel.isPresent()) {
            if (multipartFile != null) {
                String imageUrl = this.upload(multipartFile);
                userModel.get().setAvatar(imageUrl);
                userRepository.save(userModel.get());
                return ResponseEntity.ok(new ResponseObject("Create successful", userMapper.toDto(userModel.get())));
            } else {
                return ResponseEntity.ok(new ResponseObject("Image object is null", ""));
            }
        }
        return ResponseEntity.ok(new ResponseObject("User is not exist", ""));
    }

    public ResponseEntity<ResponseObject> updateAvartar(MultipartFile multipartFile, Long id) throws IOException, URISyntaxException {
        Optional<UserModel> userModel = userRepository.findById(id);
        if (userModel.isPresent()) {
            this.deleteImage(userModel.get().getAvatar());
            if (multipartFile != null) {
                String imageUrl = this.upload(multipartFile);
                userModel.get().setAvatar(imageUrl);
                userRepository.save(userModel.get());
                return ResponseEntity.ok(new ResponseObject("Update image successful", userMapper.toDto(userModel.get())));
            } else {
                return ResponseEntity.ok(new ResponseObject("Image is null", ""));
            }
        }
        return ResponseEntity.ok(new ResponseObject("User is not exist", ""));
    }

    public ResponseEntity<ResponseObject> deleteAvartar(Long id) throws IOException, URISyntaxException {
        Optional<UserModel> userModel = userRepository.findById(id);
        if (userModel.isPresent()) {
            this.deleteImage(userModel.get().getAvatar());
            String imageUrl = null;
            userModel.get().setAvatar(imageUrl);
            userRepository.save(userModel.get());
            return ResponseEntity.ok(new ResponseObject("Delete image successful", userMapper.toDto(userModel.get())));
        }
        return ResponseEntity.ok(new ResponseObject("User is not exist", ""));
    }

    private File convertToFile(MultipartFile multipartFile, String fileName) throws IOException {
        File tempFile = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
        }
        return tempFile;
    }

    private String uploadFile(File file, String fileName) throws IOException {
        BlobId blobId = BlobId.of("siin-image.appspot.com", fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
        InputStream inputStream = UserService.class.getClassLoader().getResourceAsStream("firebase.json");
        Credentials credentials = GoogleCredentials.fromStream(inputStream);
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));
        String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/siin-image.appspot.com/o/%s?alt=media";
        return String.format(DOWNLOAD_URL, URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }


    private String upload(MultipartFile multipartFile) {
        try {
            String fileName = multipartFile.getOriginalFilename();
            fileName = UUID.randomUUID().toString().concat(this.getExtension(fileName));
            File file = this.convertToFile(multipartFile, fileName);
            String URL = this.uploadFile(file, fileName);
            file.delete();
            return URL;
        } catch (Exception e) {
            e.printStackTrace();
            return "Image couldn't upload, Something went wrong";
        }
    }

    private void deleteImage(String filePath) throws IOException, URISyntaxException {
        String fileName = getFileNameFromFirebaseStorageUrl(filePath);
        BlobId blobId = BlobId.of("siin-image.appspot.com", fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build(); // Không cần set content type "media" ở đây
        InputStream inputStream = UserService.class.getClassLoader().getResourceAsStream("firebase.json");
        Credentials credentials = GoogleCredentials.fromStream(inputStream);
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();

        List<BlobId> blobIds = new ArrayList<>();
        blobIds.add(blobId);
        storage.delete(blobIds);

    }

    public static String getFileNameFromFirebaseStorageUrl(String url) throws URISyntaxException {
        String[] segments = url.split("/");
        String lastSegment = segments[segments.length - 1];
        int index = lastSegment.indexOf("?");
        if (index != -1) {
            lastSegment = lastSegment.substring(0, index);
        }
        return lastSegment;
    }
}
