package com.demo_crud.demo.dto.request.Order;

import com.demo_crud.demo.dto.request.Address.AddressCreationRequest;
import com.demo_crud.demo.dto.request.OrderItem.OrderItemUpdateRequest;
import com.demo_crud.demo.dto.request.Payment.PaymentUpdateRequest;
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
    List<OrderItemUpdateRequest> orderItems;

    String addressId;

    AddressCreationRequest address;

    PaymentUpdateRequest paymentMethod;
}
