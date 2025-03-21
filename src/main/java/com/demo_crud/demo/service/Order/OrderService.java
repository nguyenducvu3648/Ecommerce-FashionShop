package com.demo_crud.demo.service.Order;

import com.demo_crud.demo.Enum.OrderStatus;
import com.demo_crud.demo.Mapper.Order.OrderMapper;
import com.demo_crud.demo.dto.response.Order.OrderResponse;
import com.demo_crud.demo.entity.*;
import com.demo_crud.demo.exception.AppException;
import com.demo_crud.demo.exception.ErrorCode;
import com.demo_crud.demo.repository.CartItem.CartItemRepository;
import com.demo_crud.demo.repository.Order.OrderRepository;
import com.demo_crud.demo.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderService {
    OrderRepository orderRepository;
    UserService userService;
    OrderMapper orderMapper;
    CartItemRepository cartItemRepository;

    public OrderResponse createOrder(List<String> cartItemIds) {
        User user = userService.getCurrentUser();

        List<CartItem> selectedItems = cartItemRepository.findByIdInAndCartUser(cartItemIds, user);

        if (selectedItems.isEmpty()) {
            throw new AppException(ErrorCode.NO_CART_ITEMS_SELECTED);
        }

        double totalAmount = selectedItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();

        Order order = Order.builder()
                .user(user)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .orderStatus(String.valueOf(OrderStatus.PENDING))
                .totalAmount(totalAmount)
                .build();

        List<OrderItem> orderItems = selectedItems.stream().map(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice());
            orderItem.setTotalPrice(cartItem.getProduct().getPrice() * cartItem.getQuantity());
            return orderItem;
        }).toList();

        order.setOrderItems(orderItems);
        orderRepository.save(order);

        cartItemRepository.deleteAllByIdInBatch(cartItemIds);

        return orderMapper.toOrderResponse(order);
    }
}
