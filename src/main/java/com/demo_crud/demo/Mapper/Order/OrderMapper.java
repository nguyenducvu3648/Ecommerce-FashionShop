package com.demo_crud.demo.Mapper.Order;

import com.demo_crud.demo.dto.request.Order.OrderItemRequest;
import com.demo_crud.demo.dto.response.Order.OrderResponse;
import com.demo_crud.demo.entity.Order;
import com.demo_crud.demo.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "orderItems", source = "orderItems")
    @Mapping(target = "status", source = "orderStatus")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "totalAmount", expression = "java(order.getOrderItems().stream().mapToDouble(OrderItem::getTotalPrice).sum())")
    @Mapping(target = "payment", source = "payment")
    OrderResponse toOrderResponse(Order order);

    @Mapping(target = "id", ignore = true)
    OrderItem toOrderItem(OrderItemRequest request);
}
