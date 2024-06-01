package com.example.exe202backend.services;


import com.example.exe202backend.dto.AccessoryDTO;
import com.example.exe202backend.mapper.AccessoryMapper;
import com.example.exe202backend.models.Accessory;
import com.example.exe202backend.models.Product;
import com.example.exe202backend.repositories.AccessoryRepository;
import com.example.exe202backend.response.ResponseObject;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
public class AccessoryService {

    @Autowired
    private AccessoryRepository accessoryRepository;
    @Autowired
    private AccessoryMapper accessoryMapper;

    public List<AccessoryDTO> get(){
        return accessoryRepository.findAll().stream().map(accessoryMapper::toDto).collect(Collectors.toList());
    }

    public ResponseEntity<ResponseObject> create(AccessoryDTO accessoryDto) {
        Accessory accessory = accessoryMapper.toEntity(accessoryDto);
        accessoryRepository.save(accessory);
        return ResponseEntity.ok(new ResponseObject("create success",accessoryDto));
    }

    public Page<AccessoryDTO>  getAll(int currentPage, int pageSize, String field){
        Page<Accessory> accessories = accessoryRepository.findAll(
                PageRequest.of(currentPage-1,pageSize, Sort.by(Sort.Direction.ASC,field)));
        return accessories.map(accessoryMapper::toDto);
    }

    public ResponseEntity<ResponseObject> getById(long id){
        Accessory accessory = accessoryRepository.findById(id).orElseThrow(()->
                new RuntimeException("Accessory not found"));
        return ResponseEntity.ok(new ResponseObject("get success",accessoryMapper.toDto(accessory)));
    }

    public ResponseEntity<ResponseObject> delete(long id){
        Optional<Accessory> accessory = accessoryRepository.findById(id);
        if(accessory.isPresent()){
            accessory.get().setStatus(false);
            accessoryRepository.save(accessory.get());
            return ResponseEntity.ok(new ResponseObject("delete success",accessoryMapper.toDto(accessory.get())));
        }
        return ResponseEntity.badRequest().body(new ResponseObject("Accessory not found",null));
    }

    public ResponseEntity<ResponseObject> update(Long id,AccessoryDTO accessoryDto){
        Accessory existingAccessory = accessoryRepository.findById(id).orElseThrow(()
                -> new RuntimeException("Accessory not found"));
        accessoryDto.setImage(accessoryDto.getImage() != null ? accessoryDto.getImage() : existingAccessory.getImage());
        accessoryDto.setName(accessoryDto.getName() != null ? accessoryDto.getName() : existingAccessory.getName());
        accessoryDto.setPrice(accessoryDto.getPrice() != 0 ? accessoryDto.getPrice() : existingAccessory.getPrice());
        accessoryDto.setQuantity(accessoryDto.getQuantity() != 0 ? accessoryDto.getQuantity() : existingAccessory.getQuantity());
        accessoryDto.setStatus(existingAccessory.getStatus());
        accessoryMapper.updateAccessoryFromDto(accessoryDto,existingAccessory);
        accessoryRepository.save(existingAccessory);
        return ResponseEntity.ok(new ResponseObject("update success",accessoryDto));
    }

    public ResponseEntity<ResponseObject> createImage(MultipartFile multipartFile, Long id) {
        Optional<Accessory> accessory = accessoryRepository.findById(id);
        if (accessory.isPresent()) {
            if (multipartFile != null) {
                String imageUrl = this.upload(multipartFile);
                accessory.get().setImage(imageUrl);
                accessoryRepository.save(accessory.get());
                return ResponseEntity.ok(new ResponseObject("Create successful",accessoryMapper.toDto(accessory.get())));
            } else {
                return ResponseEntity.ok(new ResponseObject("Image object is null",""));
            }
        }
        return ResponseEntity.ok(new ResponseObject("Accessory is not exist",""));
    }
    public ResponseEntity<ResponseObject> updateImage(MultipartFile multipartFile, Long id) throws IOException, URISyntaxException {
        Optional<Accessory> accessory = accessoryRepository.findById(id);
        if (accessory.isPresent()) {
            this.deleteImage(accessory.get().getImage());
            if (multipartFile != null) {
                String imageUrl = this.upload(multipartFile);
                accessory.get().setImage(imageUrl);
                accessoryRepository.save(accessory.get());
                return ResponseEntity.ok(new ResponseObject("Update successful",accessoryMapper.toDto(accessory.get())));
            } else {
                return ResponseEntity.ok(new ResponseObject("Image is null",""));
            }
        }
        return ResponseEntity.ok(new ResponseObject("Accessory is not exist",""));
    }
    public ResponseEntity<ResponseObject> deleteImage(Long id) throws IOException, URISyntaxException {
        Optional<Accessory> accessory = accessoryRepository.findById(id);
        if (accessory.isPresent()) {
            this.deleteImage(accessory.get().getImage());
                String imageUrl = "";
                accessory.get().setImage(imageUrl);
                accessoryRepository.save(accessory.get());
                return ResponseEntity.ok(new ResponseObject("Delete successful",accessoryMapper.toDto(accessory.get())));
        }
        return ResponseEntity.ok(new ResponseObject("Accessory is not exist",""));
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
        InputStream inputStream = AccessoryService.class.getClassLoader().getResourceAsStream("firebase.json");
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
        InputStream inputStream = AccessoryService.class.getClassLoader().getResourceAsStream("firebase.json");
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

