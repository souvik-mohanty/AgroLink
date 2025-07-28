package com.AgroLink.product.controller;


import com.AgroLink.product.model.Cart;
import com.AgroLink.product.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


//Sourav: - Cart Controller class to add and other options
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products/cart")
public class CartController {

    private final CartService cartService;

    @GetMapping("/{userId}")
    public ResponseEntity<Cart> getCart(@PathVariable String userId) {
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    @PostMapping("/add")
    public ResponseEntity<Cart> addToCart(@RequestParam String userId,
                                          @RequestParam String productId,
                                          @RequestParam int quantity) {
        return ResponseEntity.ok(cartService.addToCart(userId, productId, quantity));
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Cart> removeFromCart(@RequestParam String userId,
                                               @RequestParam String productId,
                                               @RequestParam(required = false) Integer quantity){
        return ResponseEntity.ok(cartService.removeFromCart(userId, productId,quantity));
    }

}

