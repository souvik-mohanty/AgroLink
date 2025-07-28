package com.AgroLink.product.service;

import com.AgroLink.product.model.Cart;
import com.AgroLink.product.model.CartItem;
import com.AgroLink.product.model.Product;
import com.AgroLink.product.repository.CartRepository;
import com.AgroLink.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;



//Sourav: -  Cart Service class
@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;


    public Cart addToCart(String userId, String productId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0.");
        }

        Cart cart = cartRepository.findByUserId(userId)
                .orElse(Cart.builder().userId(userId).items(new ArrayList<>()).build());


        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

            cart.getItems().add(CartItem.builder()
                    .productId(productId)
                    .quantity(quantity)
                    .priceAtAddTime(product.getPricePerUnit())
                    .build());
        }
        calculateCartTotal(cart);

        return cartRepository.save(cart);
    }

    public Cart getCart(String userId) {
        return cartRepository.findByUserId(userId)
                .orElse(Cart.builder().userId(userId).items(new ArrayList<>()).total(0).build());
    }


    public Cart removeFromCart(String userId, String productId, Integer quantity) {
        Cart cart = getCart(userId);
        if (quantity != null && quantity > 0) {
            cart.getItems().removeIf(item -> {
                if (item.getProductId().equals(productId)) {
                    int updatedQty = item.getQuantity() - quantity;
                    if (updatedQty <= 0) {
                        return true; // Remove item
                    } else {
                        item.setQuantity(updatedQty);
                    }
                }
                return false;
            });
        } else{
            cart.getItems().removeIf(item -> item.getProductId().equals(productId));
        }

        calculateCartTotal(cart);
        return cartRepository.save(cart);
    }

    private void calculateCartTotal(Cart cart) {
        double total = cart.getItems().stream()
                .mapToDouble(item -> {
                    Product product = productRepository.findById(item.getProductId())
                            .orElseThrow(() -> new RuntimeException("Product not found: " + item.getProductId()));
                    return product.getPricePerUnit() * item.getQuantity();
                })
                .sum();

        cart.setTotal(total);
    }
}
