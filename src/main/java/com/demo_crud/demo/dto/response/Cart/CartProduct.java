package com.demo_crud.demo.dto.response.Cart;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartProduct {
    String productId;
    String productName;
    double productPrice;
    int quantity;
    double totalPrice;
}
