package com.demo_crud.demo.service.Order;

import com.demo_crud.demo.Enum.OrderStatus;
import com.demo_crud.demo.Enum.PaymentMethod;
import com.demo_crud.demo.Mapper.Address.AddressMapper;
import com.demo_crud.demo.Mapper.Order.OrderMapper;
import com.demo_crud.demo.dto.request.Address.AddressCreationRequest;
import com.demo_crud.demo.dto.request.Order.OrderUpdateRequest;
import com.demo_crud.demo.dto.request.OrderItem.OrderItemUpdateRequest;
import com.demo_crud.demo.dto.request.Payment.PaymentUpdateRequest;
import com.demo_crud.demo.dto.response.Address.AddressResponse;
import com.demo_crud.demo.dto.response.Order.OrderResponse;
import com.demo_crud.demo.entity.*;
import com.demo_crud.demo.exception.AppException;
import com.demo_crud.demo.exception.ErrorCode;
import com.demo_crud.demo.repository.Address.AddressRepository;
import com.demo_crud.demo.repository.CartItem.CartItemRepository;
import com.demo_crud.demo.repository.Order.OrderRepository;
import com.demo_crud.demo.repository.Payment.PaymentRepository;
import com.demo_crud.demo.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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
    PaymentRepository paymentRepository;

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
                .filter(item -> item.getProduct() != null)
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();

        Order order = Order.builder()
                .user(user)
                .createdAt(LocalDateTime.now())
                .orderStatus(OrderStatus.PENDING)
                .totalAmount(totalAmount)
                .address(null)
                .payment(null)
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

        List<String> paymentMethods = Arrays.stream(PaymentMethod.values())
                .map(Enum::name)
                .toList();


        List<AddressResponse> addressResponses = addressRepository.findAll().stream()
                .map(addressMapper::toAddressResponse)
                .toList();

        OrderResponse orderResponse = orderMapper.toOrderResponse(order);
        orderResponse.setAvailableAddresses(addressResponses);
        orderResponse.setPaymentMethod(paymentMethods);

        return orderResponse;
    }

    @Transactional
    public OrderResponse updateOrder(String orderId, OrderUpdateRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        updateOrderItems(order, request.getOrderItems());

        updateOrderAddress(order, request.getAddressId(), request.getAddress());

        if (request.getPaymentMethod() != null) {
            updateOrderPayment(order, request.getPaymentMethod());
        }

        orderRepository.save(order);
        return orderMapper.toOrderResponse(order);
    }
    private void updateOrderItems(Order order, List<OrderItemUpdateRequest> orderItemRequests) {
        if (orderItemRequests == null || orderItemRequests.isEmpty()) {
            return;
        }
        Map<String, OrderItem> existingItems = order.getOrderItems().stream()
                .collect(Collectors.toMap(OrderItem::getId, item -> item));

        for (OrderItemUpdateRequest itemRequest : orderItemRequests) {
            OrderItem orderItem = existingItems.get(itemRequest.getId());

            if (orderItem == null) {
                throw new AppException(ErrorCode.ORDER_ITEM_NOT_FOUND);
            }
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setTotalPrice(orderItem.getPrice() * itemRequest.getQuantity());
        }
    }
    private void updateOrderAddress(Order order, String addressId, AddressCreationRequest addressRequest) {
        if (addressId != null) {
            Address existingAddress = addressRepository.findById(addressId)
                    .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_FOUND));
            order.setAddress(existingAddress);
        } else if (addressRequest != null) {
            Address newAddress = addressMapper.toAddress(addressRequest);
            addressRepository.save(newAddress);
            order.setAddress(newAddress);
        }
    }
    private void updateOrderPayment(Order order, PaymentUpdateRequest paymentRequest) {
        if (paymentRequest.getPaymentMethod() != null) {
            Payment payment = order.getPayment();
            if (payment == null) {
                payment = new Payment();
                payment.setOrder(order);
            }
            payment.setPaymentMethod(PaymentMethod.valueOf(paymentRequest.getPaymentMethod().toUpperCase()));
            paymentRepository.save(payment);
            order.setPayment(payment);
        }
    }

    @Transactional
    public OrderResponse confirmOrder(String id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        if (!order.getOrderStatus().equals(OrderStatus.PENDING)) {
            throw new AppException(ErrorCode.ORDER_CANNOT_BE_CONFIRMED);
        }

        order.setOrderStatus(OrderStatus.READY_FOR_PAYMENT);

        orderRepository.save(order);
        return orderMapper.toOrderResponse(order);
    }


    @Transactional
    public OrderResponse cancelOrder(String id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        if(!order.getOrderStatus().equals(OrderStatus.PENDING)) {
            throw new AppException(ErrorCode.ORDER_CANNOT_BE_CONFIRMED);
        }
        order.setOrderStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
        return orderMapper.toOrderResponse(order);
    }


    public void deleteOrder(String id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        if (!order.getOrderStatus().equals(OrderStatus.CANCELLED)) {
            throw new AppException(ErrorCode.ORDER_CANNOT_BE_DELETED);
        }
        orderRepository.delete(order);
    }

    public List<OrderResponse> getMyOrder() {
        User currentUser = userService.getCurrentUser();
        List<Order> orders = orderRepository.findOrderByUser(currentUser);
        if (orders.isEmpty()) {
            return new ArrayList<>();
        }
        return orders.stream()
                .map(orderMapper::toOrderResponse)
                .collect(Collectors.toList());
    }
}
