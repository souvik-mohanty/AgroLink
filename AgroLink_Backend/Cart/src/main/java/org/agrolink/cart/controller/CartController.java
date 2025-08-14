package org.agrolink.cart.controller;



import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.agrolink.cart.dto.CartResponse;
import org.agrolink.cart.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


//Sourav: - Cart Controller class to add and other options
@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartResponse> getCartByUserId(@RequestParam String userId) {
        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }


    //calling product-service to add and remove items from cart
    @PostMapping("/add")
    @CircuitBreaker(name = "productServiceCircuitBreaker", fallbackMethod = "fallbackAddToCart")
    @RateLimiter(name = "productServiceRateLimiter", fallbackMethod = "fallbackAddToCart")
    public ResponseEntity<CartResponse> addToCart(@RequestParam String userId,
                                          @RequestParam String productId,
                                          @RequestParam int quantity) {
        return ResponseEntity.ok(cartService.addToCart(userId, productId, quantity));
    }

    //Fallback method for addToCart
    // Fallback method for addToCart
    public ResponseEntity<CartResponse> fallbackAddToCart(String userId, String productId, int quantity, Throwable throwable) {
        System.err.println("Fallback method called for addToCart: " + throwable.getMessage());
        CartResponse fallbackResponse = new CartResponse();
        fallbackResponse.setMessage("Service unavailable, please try again later.");
        return ResponseEntity.internalServerError().body(fallbackResponse);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<CartResponse> removeFromCart(@RequestParam String userId,
                                               @RequestParam String productId,
                                               @RequestParam(required = false) Integer quantity){
        return ResponseEntity.ok(cartService.removeFromCart(userId, productId,quantity));
    }

}

