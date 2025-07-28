package com.AgroLink.product.service;

import com.AgroLink.product.dto.ProductRequest;
import com.AgroLink.product.dto.ProductResponse;
import com.AgroLink.product.model.Product;
import com.AgroLink.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public ProductResponse addProduct(ProductRequest request) {
        Product product = Product.builder()
                .farmerId(request.getFarmerId())
                .name(request.getName())
                .category(request.getCategory())
                .pricePerUnit(request.getPricePerUnit())
                .quantityAvailable(request.getQuantityAvailable())
                .imageUrls(request.getImageUrls())
                .qualityTag(request.getQualityTag())
                .cropInfo(request.getCropInfo())
                .build();
        Product saved = productRepository.save(product);
        return mapToResponse(saved);
    }

    public List<ProductResponse> getAllProducts(String category) {
        return productRepository.findByCategory(category)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public ProductResponse getProduct(String id) {
        return productRepository.findById(id)
                .map(this::mapToResponse)
                .orElse(null);
    }

    public void deleteProduct(String id) {
        productRepository.deleteById(id);
    }

    private ProductResponse mapToResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .category(product.getCategory())
                .pricePerUnit(product.getPricePerUnit())
                .quantityAvailable(product.getQuantityAvailable())
                .imageUrls(product.getImageUrls())
                .qualityTag(product.getQualityTag())
                .cropInfo(product.getCropInfo())
                .build();
    }
}
