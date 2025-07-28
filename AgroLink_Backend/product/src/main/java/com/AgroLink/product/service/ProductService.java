package com.AgroLink.product.service;

import com.AgroLink.product.dto.ProductRequest;
import com.AgroLink.product.dto.ProductResponse;
import com.AgroLink.product.model.Product;
import com.AgroLink.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
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

    List<Product> products;
    if (category == null || category.isEmpty()) {
        products = productRepository.findAll(); // Get all products
    } else {
        products = productRepository.findByCategory(category);
    }
    return products.stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());

    }

    public ProductResponse getProduct(String id) {
        return productRepository.findById(id)
                .map(this::mapToResponse)
                .orElse(null);
    }

    //sourav :- This method is used to delete a product by its ID.
    public Optional<Boolean> deleteProduct(String id) {
        try {
            if (!productRepository.existsById(id)) {
                return Optional.empty(); // Product not found
            }

            productRepository.deleteById(id);
            return Optional.of(true); // Deleted successfully

        } catch (Exception e) {
            System.err.println("ProductService Delete error: " + e.getClass().getSimpleName() + "\n" + e.getMessage());
            throw new RuntimeException("Delete failed");
        }
    }

    public Optional<Boolean> updateProduct(String id, ProductRequest request) {
        try {
            Optional<Product> optionalProduct = productRepository.findById(id);
            if (optionalProduct.isEmpty()) {
                return Optional.empty();
            }

            Product product = optionalProduct.get();

            updateIfPresent(request.getName(), product::setName);
            updateIfPresent(request.getCategory(), product::setCategory);
            updateIfPresent(request.getQualityTag(), product::setQualityTag);
            updateIfPresent(request.getCropInfo(), product::setCropInfo);
            updateIfPresent(request.getFarmerId(), product::setFarmerId);
            updateIfPresent(request.getPricePerUnit(), product::setPricePerUnit);
            updateIfPresent(request.getQuantityAvailable(), product::setQuantityAvailable);
            updateIfPresent(request.getImageUrls(), product::setImageUrls);

            productRepository.save(product);
            return Optional.of(true);

        } catch (Exception e) {
            System.err.println("ProductService Update error: "
                    + e.getClass().getSimpleName() + "\n" + e.getMessage());
            throw new RuntimeException("Error occurred while updating product");
        }
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

    private void updateIfPresent(String value, Consumer<String> setter) {
        Optional.ofNullable(value)
                .filter(val -> !val.trim().isEmpty())
                .ifPresent(setter);
    }

    private <T> void updateIfPresent(T value, Consumer<T> setter) {
        Optional.ofNullable(value).ifPresent(setter);
    }


}
