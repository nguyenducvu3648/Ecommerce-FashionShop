package com.demo_crud.demo.dto.response.Cart;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItemResponse {
    String productId;
    String productName;
    double productPrice;
    int quantity;
    double totalPrice;
}
