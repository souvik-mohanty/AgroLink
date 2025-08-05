package org.agrolink.cart.controller;



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

    @PostMapping("/add")
    public ResponseEntity<CartResponse> addToCart(@RequestParam String userId,
                                          @RequestParam String productId,
                                          @RequestParam int quantity) {
        return ResponseEntity.ok(cartService.addToCart(userId, productId, quantity));
    }

    @DeleteMapping("/remove")
    public ResponseEntity<CartResponse> removeFromCart(@RequestParam String userId,
                                               @RequestParam String productId,
                                               @RequestParam(required = false) Integer quantity){
        return ResponseEntity.ok(cartService.removeFromCart(userId, productId,quantity));
    }

}

