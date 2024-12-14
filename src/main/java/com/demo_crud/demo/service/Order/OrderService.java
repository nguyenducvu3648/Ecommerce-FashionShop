package com.demo_crud.demo.service.Order;

import com.demo_crud.demo.entity.Order;
import com.demo_crud.demo.repository.Order.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderService {
    OrderRepository orderRepository;

//    @Transactional
//    public Order createOrderFromCart(String userId){
//        User currentUser = getCurrentUser();
//    }
}
