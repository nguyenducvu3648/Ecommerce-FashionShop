package com.demo_crud.demo.service.Order;

import com.demo_crud.demo.Enum.OrderStatus;
import com.demo_crud.demo.Mapper.Address.AddressMapper;
import com.demo_crud.demo.Mapper.Order.OrderMapper;
import com.demo_crud.demo.dto.response.Address.AddressResponse;
import com.demo_crud.demo.dto.response.Order.OrderResponse;
import com.demo_crud.demo.entity.*;
import com.demo_crud.demo.exception.AppException;
import com.demo_crud.demo.exception.ErrorCode;
import com.demo_crud.demo.repository.AddressRepository;
import com.demo_crud.demo.repository.CartItem.CartItemRepository;
import com.demo_crud.demo.repository.Order.OrderRepository;
import com.demo_crud.demo.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
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
    AddressRepository addressRepository;
    AddressMapper addressMapper;

    public OrderResponse createOrder(List<String> cartItemIds) {
        if (cartItemIds == null || cartItemIds.isEmpty()) {
            throw new AppException(ErrorCode.NO_CART_ITEMS_SELECTED);
        }

        User user = userService.getCurrentUser();

        List<CartItem> selectedItems = cartItemRepository.findByIdInAndCartUser(cartItemIds, user);

        if (selectedItems.isEmpty()) {
            throw new AppException(ErrorCode.NO_CART_ITEMS_FOUND);
        }

        double totalAmount = selectedItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();

        Order order = Order.builder()
                .user(user)
                .createdAt(LocalDateTime.now())
                .orderStatus(OrderStatus.PENDING)
                .totalAmount(totalAmount)
                .address(null)
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

        List<AddressResponse> addressResponses = addressRepository.findAll().stream()
                .map(addressMapper::toAddressResponse)
                .toList();

        OrderResponse orderResponse = orderMapper.toOrderResponse(order);
        orderResponse.setAvailableAddresses(addressResponses);

        return orderResponse;
    }

}
