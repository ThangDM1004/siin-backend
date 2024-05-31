package com.example.exe202backend.services;

import com.example.exe202backend.dto.ProductDTO;
import com.example.exe202backend.mapper.ProductMapper;
import com.example.exe202backend.models.Product;
import com.example.exe202backend.repositories.AccessoryRepository;
import com.example.exe202backend.repositories.ProductCategoryRepository;
import com.example.exe202backend.repositories.ProductMaterialRepository;
import com.example.exe202backend.repositories.ProductRepository;
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
import java.util.stream.Collectors;
import java.util.UUID;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductMaterialService productMaterialService;
    @Autowired
    private ProductCategoryRepository productCategoryRepository;
    @Autowired
    private AccessoryRepository accessoryRepository;
    @Autowired
    private ProductMaterialRepository productMaterialRepository;

    public List<ProductDTO> get() {
        return productRepository.findAll().stream().map(productMapper::toDto).collect(Collectors.toList());
    }

    public ResponseEntity<ResponseObject> create(ProductDTO productDTO) {
        Product product = productMapper.toEntity(productDTO);
        productRepository.save(product);
        return ResponseEntity.ok(new ResponseObject("create success", productDTO));
    }

    public Page<ProductDTO> getAll(int currentPage, int pageSize, String field) {
        Page<Product> accessories = productRepository.findAll(
                PageRequest.of(currentPage - 1, pageSize, Sort.by(Sort.Direction.ASC, field)));
        return accessories.map(productMapper::toDto);
    }

    public ResponseEntity<ResponseObject> getById(long id) {
        Product product = productRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Product not found"));
        return ResponseEntity.ok(new ResponseObject("get success", productMapper.toDto(product)));
    }

    public ResponseEntity<ResponseObject> delete(long id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            product.get().setStatus(false);
            productRepository.save(product.get());
            return ResponseEntity.ok(new ResponseObject("delete success", productMapper.toDto(product.get())));
        }
        return ResponseEntity.badRequest().body(new ResponseObject("Product not found", null));
    }

    public ResponseEntity<ResponseObject> update(Long id, ProductDTO productDTO) {
        Product existingProduct = productRepository.findById(id).orElseThrow(()
                -> new RuntimeException("Product not found"));
        productMapper.updateProductFromDto(productDTO, existingProduct);
        existingProduct.setProductMaterial(productMaterialRepository.findById(productDTO.getMaterialId()).get());
        existingProduct.setCategory(productCategoryRepository.findById(productDTO.getCategoryId()).get());
        existingProduct.setAccessory(accessoryRepository.findById(productDTO.getAccessoryId()).get());
        productRepository.save(existingProduct);
        return ResponseEntity.ok(new ResponseObject("update success", productDTO));
    }

    public ResponseEntity<ResponseObject> createCoverImage(MultipartFile multipartFile, Long id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            if (multipartFile != null) {
                String imageUrl = this.upload(multipartFile);
                product.get().setCoverImage(imageUrl);
                productRepository.save(product.get());
                return ResponseEntity.ok(new ResponseObject("Create successful", productMapper.toDto(product.get())));
            } else {
                return ResponseEntity.ok(new ResponseObject("Image object is null", ""));
            }
        }
        return ResponseEntity.ok(new ResponseObject("Product is not exist", ""));
    }

    public ResponseEntity<ResponseObject> updateCoverImage(MultipartFile multipartFile, Long id) throws IOException, URISyntaxException {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            this.deleteImage(product.get().getCoverImage());
            if (multipartFile != null) {
                String imageUrl = this.upload(multipartFile);
                product.get().setCoverImage(imageUrl);
                productRepository.save(product.get());
                return ResponseEntity.ok(new ResponseObject("Update image successful", productMapper.toDto(product.get())));
            } else {
                return ResponseEntity.ok(new ResponseObject("Image is null", ""));
            }
        }
        return ResponseEntity.ok(new ResponseObject("Product is not exist", ""));
    }

    public ResponseEntity<ResponseObject> deleteCoverImage(Long id) throws IOException, URISyntaxException {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            this.deleteImage(product.get().getCoverImage());
            String imageUrl = null;
            product.get().setCoverImage(imageUrl);
            productRepository.save(product.get());
            return ResponseEntity.ok(new ResponseObject("Delete image successful", productMapper.toDto(product.get())));
        }
        return ResponseEntity.ok(new ResponseObject("Product is not exist", ""));
    }

    public Product isExist(long accessoryId, String size, String colorName) {
        Long materialId = productMaterialService.getMaterialIdBySizeAndColorName(size, colorName);
        if (materialId == null) {
            return null;
        }
        List<Product> allProducts = productRepository.findAll();

        List<Product> filteredProducts = allProducts.stream()
                .filter(product -> product.getAccessory() != null && product.getAccessory().getId() == accessoryId
                        && product.getProductMaterial() != null && product.getProductMaterial().getId() == materialId)
                .toList();
        if (!filteredProducts.isEmpty()) {
            return filteredProducts.get(0);
        }
        return null;
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
        InputStream inputStream = ProductService.class.getClassLoader().getResourceAsStream("firebase.json");
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
        InputStream inputStream = ProductService.class.getClassLoader().getResourceAsStream("firebase.json");
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
