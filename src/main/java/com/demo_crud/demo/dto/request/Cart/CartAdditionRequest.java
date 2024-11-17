package com.demo_crud.demo.dto.request.Cart;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartAdditionRequest {
    UUID productId;
    int quantity;
}