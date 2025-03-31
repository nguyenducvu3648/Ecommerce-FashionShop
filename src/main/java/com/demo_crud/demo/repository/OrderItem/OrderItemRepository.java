package com.demo_crud.demo.repository.OrderItem;

import com.demo_crud.demo.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, String> {
}
