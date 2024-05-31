package com.example.exe202backend.services;

import com.example.exe202backend.dto.ProductSubImageDTO;
import com.example.exe202backend.mapper.ProductSubImageMapper;
import com.example.exe202backend.models.Product;
import com.example.exe202backend.models.ProductSubImage;
import com.example.exe202backend.repositories.ProductRepository;
import com.example.exe202backend.repositories.ProductSubImageRepository;
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
public class ProductSubImageService {
    @Autowired
    private ProductSubImageRepository productSubImageRepository;
    @Autowired
    private ProductSubImageMapper productSubImageMapper;
    @Autowired
    private ProductRepository productRepository;

    public List<ProductSubImageDTO> get() {
        return productSubImageRepository.findAll().stream().map(productSubImageMapper::toDto).collect(Collectors.toList());
    }

    public ResponseEntity<ResponseObject> create(ProductSubImageDTO productSubImageDTO) {
        ProductSubImage productSubImage = productSubImageMapper.toEntity(productSubImageDTO);
        productSubImageRepository.save(productSubImage);
        return ResponseEntity.ok(new ResponseObject("create success", productSubImageDTO));
    }

    public Page<ProductSubImageDTO> getAll(int currentPage, int pageSize, String field) {
        Page<ProductSubImage> productSubImages = productSubImageRepository.findAll(
                PageRequest.of(currentPage - 1, pageSize, Sort.by(Sort.Direction.ASC, field)));
        return productSubImages.map(productSubImageMapper::toDto);
    }

    public ResponseEntity<ResponseObject> getById(long id) {
        ProductSubImage productSubImage = productSubImageRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Product Sub Image not found"));
        return ResponseEntity.ok(new ResponseObject("get success", productSubImageMapper.toDto(productSubImage)));
    }

    public ResponseEntity<ResponseObject> delete(long id) {
        Optional<ProductSubImage> productSubImage = productSubImageRepository.findById(id);
        if (productSubImage.isPresent()) {
            productSubImage.get().setStatus(false);
            productSubImageRepository.save(productSubImage.get());
            return ResponseEntity.ok(new ResponseObject("delete success", productSubImageMapper.toDto(productSubImage.get())));
        }
        return ResponseEntity.badRequest().body(new ResponseObject("Product not found", null));
    }

    public ResponseEntity<ResponseObject> update(Long id, ProductSubImageDTO productSubImageDTO) {
        ProductSubImage productSubImage = productSubImageRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Product Sub Image not found"));
        productSubImageMapper.updateProductSubImageFromDto(productSubImageDTO, productSubImage);
        productSubImageRepository.save(productSubImage);
        return ResponseEntity.ok(new ResponseObject("update success", productSubImageDTO));
    }

    public ResponseEntity<ResponseObject> getByProductId(Long productId) {
        ArrayList<ProductSubImage> list = productSubImageRepository.findProductSubImageByProduct_Id(productId);

        if (list.isEmpty()) {
            return ResponseEntity.ok(new ResponseObject("Product don't have image", ""));
        } else {
            List<ProductSubImageDTO> _list = list.stream().map(productSubImageMapper::toDto).toList();
            return ResponseEntity.ok(new ResponseObject("Get list image success", _list));
        }
    }

    public ResponseEntity<ResponseObject> createSubImage(List<MultipartFile> multipartFile, Long productId) {
        Optional<Product> product = productRepository.findById(productId);
        List<ProductSubImageDTO> list = new ArrayList<>();
        if (product.isPresent()) {
            if (multipartFile != null) {
                for (MultipartFile x : multipartFile) {
                    ProductSubImage productSubImage = new ProductSubImage();
                    String imageUrl = this.upload(x);
                    productSubImage.setProduct(product.get());
                    productSubImage.setUrl(imageUrl);
                    productSubImage.setStatus(true);
                    productSubImageRepository.save(productSubImage);
                    list.add(productSubImageMapper.toDto(productSubImage));
                }
                return ResponseEntity.ok(new ResponseObject("Create successful", list));
            } else {
                return ResponseEntity.ok(new ResponseObject("Image is null", ""));
            }
        }
        return ResponseEntity.ok(new ResponseObject("Product is not exist", ""));
    }

    public ResponseEntity<ResponseObject> updateImage(MultipartFile multipartFile, Long id) throws IOException, URISyntaxException {
        Optional<ProductSubImage> productSubImage = productSubImageRepository.findById(id);
        if (productSubImage.isPresent()) {
            this.deleteImage(productSubImage.get().getUrl());
            if (multipartFile != null) {
                String imageUrl = this.upload(multipartFile);
                productSubImage.get().setUrl(imageUrl);
                productSubImageRepository.save(productSubImage.get());
                return ResponseEntity.ok(new ResponseObject("Update image successful", productSubImageMapper.toDto(productSubImage.get())));
            } else {
                return ResponseEntity.ok(new ResponseObject("Image is null", ""));
            }
        }
        return ResponseEntity.ok(new ResponseObject("Product image is not exist", ""));
    }

    public ResponseEntity<ResponseObject> deleteImage(Long id) throws IOException, URISyntaxException {
        Optional<ProductSubImage> productSubImage = productSubImageRepository.findById(id);
        if (productSubImage.isPresent()) {
            this.deleteImage(productSubImage.get().getUrl());
            String imageUrl = null;
            productSubImage.get().setUrl(imageUrl);
            productSubImageRepository.save(productSubImage.get());
            return ResponseEntity.ok(new ResponseObject("Delete image successful", productSubImageMapper.toDto(productSubImage.get())));
        }
        return ResponseEntity.ok(new ResponseObject("Product image is not exist", ""));
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
        InputStream inputStream = ProductSubImageService.class.getClassLoader().getResourceAsStream("firebase.json");
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
        InputStream inputStream = ProductSubImageService.class.getClassLoader().getResourceAsStream("firebase.json");
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
