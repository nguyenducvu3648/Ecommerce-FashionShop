package com.demo_crud.demo.repository.Payment;

import com.demo_crud.demo.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, String> {
}
