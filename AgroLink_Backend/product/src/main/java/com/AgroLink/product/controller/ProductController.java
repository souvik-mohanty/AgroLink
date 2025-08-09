package com.AgroLink.product.controller;

import com.AgroLink.product.dto.ProductRequest;
import com.AgroLink.product.dto.ProductResponse;
import com.AgroLink.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse addProduct(@Valid @ModelAttribute ProductRequest request,
                                      @RequestParam("images") List<MultipartFile> images) throws IOException {
        return productService.addProduct(request, images);
    }


    @GetMapping
    public List<ProductResponse> getProducts(@RequestParam(required = false) String category) {
        return productService.getAllProducts(category);
    }

    @GetMapping("/{id}")
    public ProductResponse getProduct(@PathVariable String id) {
        return productService.getProduct(id);
    }
    @GetMapping("/photo/{id}")
    public ResponseEntity<byte[]> getProductPhoto(@PathVariable String id) {
        return productService.getProductPhoto(id);
    }

    @PutMapping(value = "/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ProductResponse updateProduct(@PathVariable String id,
                                         @Valid @ModelAttribute ProductRequest request,
                                         @RequestParam("images") List<MultipartFile> images) throws IOException {
        return productService.updateProduct(id, request, images);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}