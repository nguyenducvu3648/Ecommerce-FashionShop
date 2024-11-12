package com.demo_crud.demo.dto.request.Cart;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CartAdditionRequest {
    String productId;
    int quantity;
}
