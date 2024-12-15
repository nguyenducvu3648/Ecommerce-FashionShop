package com.demo_crud.demo.dto.response.Order;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {
    private String id;
    private List<OrderItemResponse> orderItems;
    private double totalAmount;
    private String status;
    private LocalDateTime createdAt;
}
