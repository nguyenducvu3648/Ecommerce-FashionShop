package com.demo_crud.demo.Mapper.Order;

import com.demo_crud.demo.dto.request.Order.OrderItemRequest;
import com.demo_crud.demo.dto.response.Order.OrderResponse;
import com.demo_crud.demo.entity.Order;
import com.demo_crud.demo.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderResponse toOrderResponse(Order order);

    @Mapping(target = "id", ignore = true)
    OrderItem toOrderItem(OrderItemRequest request);
}
