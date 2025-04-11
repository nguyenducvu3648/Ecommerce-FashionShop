package com.demo_crud.demo.repository.Order;

import com.demo_crud.demo.entity.Order;
import com.demo_crud.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findOrderByUser(User user);
}
