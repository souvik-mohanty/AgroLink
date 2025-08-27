package org.agrolink.cart.controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; // UPDATE: Added for proper logging
import org.agrolink.cart.dto.CartResponse;
import org.agrolink.cart.service.CartService;
import org.springframework.http.HttpStatus; // UPDATE: Added for Service Unavailable status
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
@Slf4j // UPDATE: Added SLF4J for proper logging
public class CartController {

    private final CartService cartService;

    // UPDATE: Using a path variable for a more RESTful URL
    @GetMapping
    public ResponseEntity<CartResponse> getCartByUserId(@RequestParam String userId) {
        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }

    // UPDATE: A more RESTful endpoint for adding an item to a specific user's cart
    @PostMapping("/add")
//    @RateLimiter(name = "productServiceRateLimiter", fallbackMethod = "fallbackAddToCartRateLimit")
    @CircuitBreaker(name = "productServiceCircuitBreaker", fallbackMethod = "fallbackAddToCartServiceDown")
    public ResponseEntity<CartResponse> addToCart(@RequestParam String userId,
                                                  @RequestParam String productId,
                                                  @RequestParam int quantity) {
        return ResponseEntity.ok(cartService.addToCart(userId, productId, quantity));
    }
    // Fallback for when the downstream service is down
    public ResponseEntity<CartResponse> fallbackAddToCartServiceDown(String userId, String productId,
                                                                     int quantity, Throwable throwable) {
        log.error("Fallback due to service failure for user: {}. Reason: {}", userId, throwable.getMessage());
        CartResponse fallbackResponse = new CartResponse();
        fallbackResponse.setMessage("Service is temporarily unavailable, please try again later.");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(fallbackResponse);
    }

    // Fallback for when the rate limit is exceeded
    public ResponseEntity<CartResponse> fallbackAddToCartRateLimit(String userId, String productId,
                                                                   int quantity, Throwable throwable) {
        log.warn("Rate limit fallback triggered for user: {}", userId);
        CartResponse fallbackResponse = new CartResponse();
        fallbackResponse.setMessage("Too many requests. Please try again in a moment.");
        // Return a 429 Too Many Requests status
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(fallbackResponse);
    }

    // UPDATE: A more RESTful endpoint for removing an item from a cart
    @DeleteMapping("/remove")
    public ResponseEntity<CartResponse> removeFromCart(@RequestParam String userId,
                                                       @RequestParam String productId,
                                                       @RequestParam(required = false) Integer quantity){
        return ResponseEntity.ok(cartService.removeFromCart(userId, productId, quantity));
    }


}