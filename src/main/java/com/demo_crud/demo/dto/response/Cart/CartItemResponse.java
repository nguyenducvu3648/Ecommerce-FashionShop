package com.demo_crud.demo.dto.response.Cart;

import com.demo_crud.demo.dto.response.ProductResponse.ProductResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItemResponse {
    String cartItemId;
    ProductResponse productResponse;
    int quantity;
    double totalPrice;
}
