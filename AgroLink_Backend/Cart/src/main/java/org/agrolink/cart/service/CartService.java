package org.agrolink.cart.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.agrolink.cart.dto.CartResponse;
import org.agrolink.cart.dto.ProductResponse;
import org.agrolink.cart.feign.ProductClient;
import org.agrolink.cart.model.Cart;
import org.agrolink.cart.model.CartItem;
import org.agrolink.cart.repository.CartRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {

    private final CartRepository cartRepository;
    private final ProductClient productClient;



    public CartResponse addToCart(String userId, String productId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0.");
        }

        // Step 1: Fetch Product Details from the product-service.
        // This single API call confirms existence and gets all required data.
        ProductResponse product;
        try {
            product = productClient.getProduct(productId);
        } catch (FeignException.NotFound e) {
            throw new RuntimeException("Cannot add to cart. Product not found: " + productId, e);
        }

        // Step 2: Handle database operations and core business logic.
        try {
            Cart cart = cartRepository.findByUserId(userId)
                    .orElse(Cart.builder().userId(userId).items(new ArrayList<>()).build());

            Optional<CartItem> existingItemOpt = cart.getItems().stream()
                    .filter(item -> item.getProductId().equals(productId))
                    .findFirst();

            // Calculate the total required quantity, including items already in the cart.
            int quantityAlreadyInCart = existingItemOpt.map(CartItem::getQuantity).orElse(0);
            int totalRequiredQuantity = quantityAlreadyInCart + quantity;

            // Check if the required quantity exceeds the available stock.
            if (totalRequiredQuantity > product.getQuantityAvailable()) {
                throw new IllegalArgumentException(
                        String.format("Insufficient stock for product %s. Requested total: %d, Available: %d",
                                productId, totalRequiredQuantity, product.getQuantityAvailable())
                );
            }

            // Update existing item's quantity or add a new item.
            if (existingItemOpt.isPresent()) {
                existingItemOpt.get().setQuantity(totalRequiredQuantity);
            } else {
                cart.getItems().add(CartItem.builder()
                        .productId(productId)
                        .quantity(quantity)
                        .priceAtAddTime(product.getPricePerUnit()) // Lock in the price
                        .build());
            }

            calculateCartTotal(cart);
            Cart savedCart = cartRepository.save(cart);
            return mapToCartResponse(savedCart);

        } catch (DataAccessException e) {
            log.error("Database error in addToCart for user {}: {}", userId, e.getMessage());
            throw new RuntimeException("Service is temporarily unavailable. Please try again later.", e);
        }
    }


    public CartResponse getCartByUserId(String userId) {
        try {
            return cartRepository.findByUserId(userId)
                    .map(this::mapToCartResponse)
                    .orElse(CartResponse.builder().userId(userId).items(new ArrayList<>()).total(0.0).build());
        } catch (DataAccessException e) {
            log.error("Database error in getCartByUserId for user {}: {}", userId, e.getMessage());
            throw new RuntimeException("Service is temporarily unavailable. Please try again later.", e);
        }
    }



    public CartResponse removeFromCart(String userId, String productId, Integer quantity) {
        try {
            Cart cart = findCartByUserId(userId);
            if (quantity != null && quantity > 0) {
                cart.getItems().removeIf(item -> {
                    if (item.getProductId().equals(productId)) {
                        int updatedQty = item.getQuantity() - quantity;
                        if (updatedQty <= 0) {
                            return true; // Remove item completely
                        } else {
                            item.setQuantity(updatedQty); // Otherwise, just update quantity
                        }
                    }
                    return false;
                });
            } else {
                cart.getItems().removeIf(item -> item.getProductId().equals(productId));
            }
            calculateCartTotal(cart);
            Cart savedCart = cartRepository.save(cart);
            return mapToCartResponse(savedCart);
        } catch (DataAccessException e) {
            log.error("Database error in removeFromCart for user {}: {}", userId, e.getMessage());
            throw new RuntimeException("Service is temporarily unavailable. Please try again later.", e);
        }
    }

    /**
     * Calculates the cart's total based on the price stored AT THE TIME THE ITEM WAS ADDED.
     */
    private void calculateCartTotal(Cart cart) {
        double total = cart.getItems().stream()
                .mapToDouble(item -> item.getPriceAtAddTime() * item.getQuantity())
                .sum();
        cart.setTotal(total);
    }

    /**
     * Helper method to find a Cart entity or throw a standard exception.
     */
    private Cart findCartByUserId(String userId) {
        // This is a private helper, the DataAccessException is handled by the public methods calling it.
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user: " + userId));
    }


    private CartResponse mapToCartResponse(Cart cart) {
        return CartResponse.builder()
                .Id(cart.getId())
                .userId(cart.getUserId())
                .items(cart.getItems())
                .total(cart.getTotal())
                .build();
    }
}