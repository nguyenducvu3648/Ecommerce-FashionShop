package com.demo_crud.demo.dto.request.Order;

import com.demo_crud.demo.Enum.OrderStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderUpdateRequest {
    private List<OrderItemRequest> orderItems;
    private OrderStatus status;
}
