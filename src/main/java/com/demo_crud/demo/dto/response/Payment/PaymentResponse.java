package com.demo_crud.demo.dto.response.Payment;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class PaymentResponse {
    String id;
    String paymentMethod;
    String status;
    String transactionId;
    double amount;
    LocalDateTime createdAt;
    LocalDateTime paidAt;
}
