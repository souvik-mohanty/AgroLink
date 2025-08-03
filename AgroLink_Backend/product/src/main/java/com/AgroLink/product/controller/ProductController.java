package com.AgroLink.product.controller;

import com.AgroLink.product.dto.ProductRequest;
import com.AgroLink.product.dto.ProductResponse;
import com.AgroLink.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ProductResponse addProduct(@RequestBody ProductRequest request) {
        return productService.addProduct(request);
    }

    @GetMapping
    public List<ProductResponse> getProducts(@RequestParam(required = false) String category) {
        return productService.getAllProducts(category);
    }

    @GetMapping("/{id}")
    public ProductResponse getProduct(@PathVariable String id) {
        return productService.getProduct(id);
    }

    // sourav :- update the updateProduct method to return a ProductResponse
    @PutMapping("/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable String id, @RequestBody ProductRequest request) {
        try {
            return productService.updateProduct(id, request)
                    .map(updated -> ResponseEntity.status(204)
                            .body("User updated successfully")) // 204 No Content on success
                    .orElseGet(() -> ResponseEntity.status(404)
                            .body("Product not found")); // 404 if not found
        } catch (Exception e) {
            System.err.println("ProductController Update error: "
                    + e.getClass().getSimpleName() + " - " + e.getMessage());
            return ResponseEntity.status(500).build(); // 500 on server error
        }
    }

    // sourav :- update the deleteProduct method to return a String message
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable String id) {
        try {
            return productService.deleteProduct(id)
                    .map(success -> ResponseEntity.status(200)
                            .body("Product deleted successfully"))
                    .orElseGet(() -> ResponseEntity.status(404)
                            .body("Product not found"));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("Something went wrong. Please try again later");
        }
    }

}
