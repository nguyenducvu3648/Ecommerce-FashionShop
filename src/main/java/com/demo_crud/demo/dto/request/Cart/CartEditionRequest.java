package com.demo_crud.demo.dto.request.Cart;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartEditionRequest {
        String cartItemId;
        int quantity;
}
