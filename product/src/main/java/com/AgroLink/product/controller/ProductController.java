package com.AgroLink.product.controller;

import com.AgroLink.product.dto.ProductRequest;
import com.AgroLink.product.dto.ProductResponse;
import com.AgroLink.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ProductResponse addProduct(@RequestBody ProductRequest request) {
        return productService.addProduct(request);
    }

    @GetMapping("/{id}")
    public ProductResponse getProduct(@PathVariable String id) {
        return productService.getProduct(id);
    }

    @GetMapping
    public List<ProductResponse> getProducts(@RequestParam(required = false) String category) {
        return productService.getAllProducts(category != null ? category : "");
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
    }




}
