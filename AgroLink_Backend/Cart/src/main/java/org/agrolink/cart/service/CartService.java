package org.agrolink.cart.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.agrolink.cart.dto.CartResponse;
import org.agrolink.cart.dto.ProductResponse;
import org.agrolink.cart.exception.InsufficientStockException;
import org.agrolink.cart.exception.ProductNotFoundException;
import org.agrolink.cart.exception.UserInactiveException;
import org.agrolink.cart.feign.ProductClient;
import org.agrolink.cart.feign.UserClient;
import org.agrolink.cart.model.Cart;
import org.agrolink.cart.model.CartItem;
import org.agrolink.cart.repository.CartRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional // UPDATE: Moved to the class level for consistency
public class CartService {

    private final CartRepository cartRepository;
    private final ProductClient productClient;
    private final UserClient userClient;

    public CartResponse addToCart(String userId, String productId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0.");
        }

        // UPDATE: Logic is now broken into clear, readable steps.
        validateUser(userId);
        ProductResponse product = fetchProductDetails(productId);

        try {
            Cart cart = cartRepository.findByUserId(userId)
                    .orElse(Cart.builder().userId(userId).items(new ArrayList<>()).build());

            updateCartItem(cart, product, productId, quantity);

            calculateCartTotal(cart);
            cartRepository.save(cart);
            return mapToCartResponse(cart);

        } catch (DataAccessException e) {
            log.error("Database error in addToCart for user {}: {}", userId, e.getMessage());
            throw new RuntimeException("Service is temporarily unavailable. Please try again later.", e);
        }
    }

    @Transactional(readOnly = true)
    public CartResponse getCartByUserId(String userId) {
        // ... no changes needed here
        return cartRepository.findByUserId(userId)
                .map(this::mapToCartResponse)
                .orElse(CartResponse.builder().userId(userId).items(new ArrayList<>()).total(0.0).build());
    }

    public CartResponse removeFromCart(String userId, String productId, Integer quantity) {
        // ... no changes needed here
        Cart cart = findCartByUserId(userId);
        // ... rest of the method
        return null;
    }

    // --- Private Helper Methods ---

    /**
     * UPDATE: New private method to handle user validation.
     * This makes the main addToCart method cleaner.
     */
    private void validateUser(String userId) {
        try {
            if (!userClient.userExistsAndActive(userId)) {
                log.warn("User {} does not exist or is inactive", userId);
                throw new UserInactiveException("Cannot add to cart. User is invalid or inactive.");
            }
            log.info("User {} is valid", userId);
        } catch (FeignException e) {
            log.error("User service call failed for user {}: {}", userId, e.getMessage());
            throw new RuntimeException("User service unavailable", e);
        }
    }

    /**
     * UPDATE: New private method to handle fetching product details.
     */
    private ProductResponse fetchProductDetails(String productId) {
        try {
            return productClient.getProduct(productId);
        } catch (FeignException.NotFound e) {
            log.warn("Product not found for ID: {}", productId);
            throw new ProductNotFoundException("Cannot add to cart. Product not found: " + productId);
        } catch (FeignException e) {
            log.error("Product service call failed for product {}: {}", productId, e.getMessage());
            throw new RuntimeException("Product service unavailable", e);
        }
    }

    /**
     * UPDATE: New private method to encapsulate the logic of updating or adding an item.
     */
    private void updateCartItem(Cart cart, ProductResponse product, String productId, int quantity) {
        Optional<CartItem> existingItemOpt = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();

        int quantityAlreadyInCart = existingItemOpt.map(CartItem::getQuantity).orElse(0);
        int totalRequiredQuantity = quantityAlreadyInCart + quantity;

        if (totalRequiredQuantity > product.getQuantityAvailable()) {
            throw new InsufficientStockException(
                    String.format("Insufficient stock for product %s. Requested total: %d, Available: %d",
                            productId, totalRequiredQuantity, product.getQuantityAvailable())
            );
        }

        if (existingItemOpt.isPresent()) {
            existingItemOpt.get().setQuantity(totalRequiredQuantity);
        } else {
            cart.getItems().add(CartItem.builder()
                    .productId(productId)
                    .quantity(quantity)
                    .priceAtAddTime(product.getPricePerUnit())
                    .build());
        }
    }

    private void calculateCartTotal(Cart cart) {
        double total = cart.getItems().stream()
                .mapToDouble(item -> item.getPriceAtAddTime() * item.getQuantity())
                .sum();
        cart.setTotal(total);
    }

    // UPDATE: Changed back to private for better encapsulation
    @Transactional(readOnly = true)
    private Cart findCartByUserId(String userId) {
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