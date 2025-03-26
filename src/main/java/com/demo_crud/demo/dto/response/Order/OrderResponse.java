package com.demo_crud.demo.dto.response.Order;

import com.demo_crud.demo.dto.response.Address.AddressResponse;
import com.demo_crud.demo.dto.response.Payment.PaymentResponse;
import com.demo_crud.demo.dto.response.UserResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {
    String id;
    UserResponse user;
    List<OrderItemResponse> orderItems;
    double totalAmount;
    String status;
    LocalDateTime createdAt;
    List<AddressResponse> availableAddresses;
    AddressResponse address;
    PaymentResponse payment;
}
