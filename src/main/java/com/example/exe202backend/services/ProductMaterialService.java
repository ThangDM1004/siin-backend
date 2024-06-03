package com.example.exe202backend.services;

import com.example.exe202backend.dto.ProductMaterialDTO;
import com.example.exe202backend.mapper.ProductMaterialMapper;
import com.example.exe202backend.models.ProductMaterial;
import com.example.exe202backend.repositories.ProductMaterialRepository;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductMaterialService {
    @Autowired
    private ProductMaterialRepository productMaterialRepository;
    @Autowired
    private ProductMaterialMapper productMaterialMapper;

    public List<ProductMaterialDTO> get(){
        return productMaterialRepository.findAll().stream().map(productMaterialMapper::toDto).collect(Collectors.toList());
    }

    public ResponseEntity<ResponseObject> create(ProductMaterialDTO productMaterialDTO){
        ProductMaterial productMaterial = productMaterialMapper.toEntity(productMaterialDTO);
        productMaterialRepository.save(productMaterial);
        return ResponseEntity.ok(new ResponseObject("create success", productMaterialDTO));
    }
    public Page<ProductMaterialDTO> getAll(int currentPage, int pageSize, String field){
        Page<ProductMaterial> productMaterials = productMaterialRepository.findAll(
                PageRequest.of(currentPage-1,pageSize, Sort.by(Sort.Direction.ASC,field)));
        return productMaterials.map(productMaterialMapper::toDto);
    }

    public ResponseEntity<ResponseObject> getById(long id){
        Optional<ProductMaterial> productMaterial = productMaterialRepository.findById(id);
        return productMaterial.map(material -> ResponseEntity.ok(new ResponseObject("get success", productMaterialMapper.toDto(material))))
                .orElseGet(() -> ResponseEntity.ok(new ResponseObject("get success", null)));
    }

    public ResponseEntity<ResponseObject> delete(long id){
        Optional<ProductMaterial> productMaterial = productMaterialRepository.findById(id);
        if(productMaterial.isPresent()){
            productMaterial.get().setStatus(false);
            productMaterialRepository.save(productMaterial.get());
            return ResponseEntity.ok(new ResponseObject("delete success",
                    productMaterialMapper.toDto(productMaterial.get())));
        }
        return ResponseEntity.badRequest().body(new ResponseObject("delete fail",null));
    }

    public ResponseEntity<ResponseObject> update(Long id, ProductMaterialDTO productMaterialDTO){
        ProductMaterial existingProductMaterial = productMaterialRepository.findById(id).orElseThrow(()
                -> new RuntimeException("Product material not found"));
        if(productMaterialDTO.getColorId() == null){
            productMaterialDTO.setColorId(existingProductMaterial.getColor().getId());
        }
        if(productMaterialDTO.getSizeId() == null){
            productMaterialDTO.setSizeId(existingProductMaterial.getSize().getId());
        }
        if(productMaterialDTO.getQuantity() == 0){
            productMaterialDTO.setQuantity(existingProductMaterial.getQuantity());
        }
        if(productMaterialDTO.getImage() == null){
            productMaterialDTO.setImage(existingProductMaterial.getImage());
        }
        if(productMaterialDTO.getPrice() == 0){
            productMaterialDTO.setPrice(existingProductMaterial.getPrice());
        }
        productMaterialDTO.setStatus(existingProductMaterial.getStatus());
        productMaterialMapper.updateProductMaterialFromDto(productMaterialDTO,existingProductMaterial);
        productMaterialRepository.save(existingProductMaterial);
        return ResponseEntity.ok(new ResponseObject("update success",productMaterialDTO));
    }

    public Long getMaterialIdBySizeAndColorName(Long sizeId, Long colorId) {
        List<ProductMaterial> list = productMaterialRepository.findAll();
        List<ProductMaterial> filteredList = list.stream()
                .filter(productMaterial -> Objects.equals(productMaterial.getColor().getId(), colorId)
                        && Objects.equals(productMaterial.getSize().getId(), sizeId)).toList();
        if (filteredList.isEmpty()) {
            return null;
        }
        return filteredList.get(0).getId();
    }

    public ResponseEntity<ResponseObject> createImage(MultipartFile multipartFile, Long id) {
        Optional<ProductMaterial> productMaterial = productMaterialRepository.findById(id);
        if (productMaterial.isPresent()) {
            if (multipartFile != null) {
                String imageUrl = this.upload(multipartFile);
                productMaterial.get().setImage(imageUrl);
                productMaterialRepository.save(productMaterial.get());
                return ResponseEntity.ok(new ResponseObject("Create successful",productMaterialMapper.toDto(productMaterial.get())));
            } else {
                return ResponseEntity.ok(new ResponseObject("Image object is null",""));
            }
        }
        return ResponseEntity.ok(new ResponseObject("Product Material is not exist",""));
    }
    public ResponseEntity<ResponseObject> updateImage(MultipartFile multipartFile, Long id) throws IOException, URISyntaxException {
        Optional<ProductMaterial> productMaterial = productMaterialRepository.findById(id);
        if (productMaterial.isPresent()) {
            this.deleteImage(productMaterial.get().getImage());
            if (multipartFile != null) {
                String imageUrl = this.upload(multipartFile);
                productMaterial.get().setImage(imageUrl);
                productMaterialRepository.save(productMaterial.get());
                return ResponseEntity.ok(new ResponseObject("Update successful",productMaterialMapper.toDto(productMaterial.get())));
            } else {
                return ResponseEntity.ok(new ResponseObject("Image is null",""));
            }
        }
        return ResponseEntity.ok(new ResponseObject("Product Material is not exist",""));
    }
    public ResponseEntity<ResponseObject> deleteImage(Long id) throws IOException, URISyntaxException {
        Optional<ProductMaterial> productMaterial = productMaterialRepository.findById(id);
        if (productMaterial.isPresent()) {
            this.deleteImage(productMaterial.get().getImage());
                String imageUrl = "";
                productMaterial.get().setImage(imageUrl);
                productMaterialRepository.save(productMaterial.get());
                return ResponseEntity.ok(new ResponseObject("Delete successful",productMaterialMapper.toDto(productMaterial.get())));
        }
        return ResponseEntity.ok(new ResponseObject("Product Material is not exist",""));
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
