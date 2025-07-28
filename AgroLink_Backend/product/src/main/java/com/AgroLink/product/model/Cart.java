package com.AgroLink.product.model;

import io.netty.handler.codec.socksx.v4.Socks4CommandRequest;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;


//sourav:-
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "carts")
public class Cart {
    @Id
    private String id;

    private String userId;

    private List<CartItem> items = new ArrayList<>();

    private double total;

}
