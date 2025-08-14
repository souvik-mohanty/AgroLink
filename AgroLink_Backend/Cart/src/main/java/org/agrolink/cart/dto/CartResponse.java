package org.agrolink.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.agrolink.cart.model.CartItem;
import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartResponse {
    private String Id;
    private String userId;
    private List<CartItem> items = new ArrayList<>();
    private double total;
    private String message;
}