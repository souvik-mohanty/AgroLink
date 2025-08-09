package com.AgroLink.product.service;

import com.AgroLink.product.dto.ProductRequest;
import com.AgroLink.product.dto.ProductResponse;
import com.AgroLink.product.exception.ProductNotFoundException;
import com.AgroLink.product.model.Product;
import com.AgroLink.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final PhotoService photoService;

    public ProductResponse addProduct(ProductRequest request, List<MultipartFile> files) throws IOException {

        // 2. Upload photos and collect their new IDs
        List<String> photoIds = new ArrayList<>();
        for (MultipartFile file : files) {
            // Assuming uploaderId is the same as the product's farmerId
            String photoId = photoService.addPhoto(file, request.getFarmerId());
            photoIds.add(photoId);
        }

        // 3. Build the Product object with the request data and the new photo IDs
        Product product = Product.builder()
                .farmerId(request.getFarmerId())
                .name(request.getName())
                .category(request.getCategory())
                .pricePerUnit(request.getPricePerUnit())
                .quantityAvailable(request.getQuantityAvailable())
                .photoIds(photoIds) // Use the newly generated IDs
                .qualityTag(request.getQualityTag())
                .cropInfo(request.getCropInfo())
                .build();

        Product saved = productRepository.save(product);
        return mapToResponse(saved);
    }

    public List<ProductResponse> getAllProducts(String category) {
        List<Product> products;
        if (category == null || category.trim().isEmpty()) {
            products = productRepository.findAll();
        } else {
            products = productRepository.findByCategory(category);
        }
        return products.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public ProductResponse getProduct(String id) {
        return productRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
    }

    public void deleteProduct(String id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Cannot delete. Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    public ProductResponse updateProduct(String id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Cannot update. Product not found with id: " + id));

        updateIfPresent(request.getName(), product::setName);
        updateIfPresent(request.getCategory(), product::setCategory);
        updateIfPresent(request.getQualityTag(), product::setQualityTag);
        updateIfPresent(request.getCropInfo(), product::setCropInfo);
        updateIfPresent(request.getPricePerUnit(), product::setPricePerUnit);
        updateIfPresent(request.getQuantityAvailable(), product::setQuantityAvailable);
        updateIfPresent(request.getPhotoIds(), product::setPhotoIds);

        Product updatedProduct = productRepository.save(product);
        return mapToResponse(updatedProduct);
    }

    private ProductResponse mapToResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .category(product.getCategory())
                .pricePerUnit(product.getPricePerUnit())
                .quantityAvailable(product.getQuantityAvailable())
                .photoIds(product.getPhotoIds())
                .qualityTag(product.getQualityTag())
                .cropInfo(product.getCropInfo())
                .build();
    }

    private <T> void updateIfPresent(T value, Consumer<T> setter) {
        Optional.ofNullable(value).ifPresent(setter);
    }

    public ResponseEntity<byte[]> getProductPhoto(String id) {
        // Fetch the product to get the photo IDs
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));

        // Assuming we want to return the first photo's data
        if (product.getPhotoIds() == null || product.getPhotoIds().isEmpty()) {
            throw new ProductNotFoundException("No photos available for product with id: " + id);
        }

        String photoId = product.getPhotoIds().get(0); // Get the first photo ID
        byte[] photoData = photoService.getPhoto(photoId).getImageData();

        return ResponseEntity.ok()
                .header("Content-Type", "image/jpeg") // Adjust content type as needed
                .body(photoData);
    }
}