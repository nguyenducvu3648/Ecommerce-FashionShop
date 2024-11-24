package com.demo_crud.demo.dto.response.Cart;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CartResponse {
    String user;
    String cartId;
    List<CartItemResponse> cartItems;
    double totalAmount;
}
