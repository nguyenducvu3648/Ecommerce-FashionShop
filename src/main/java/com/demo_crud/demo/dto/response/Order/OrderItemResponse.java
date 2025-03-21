package com.demo_crud.demo.dto.response.Order;

import com.demo_crud.demo.dto.response.ProductResponse.ProductResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderItemResponse {
    String id;
    ProductResponse productResponse;
    int quantity;
    double price;
    double totalPrice;
}
