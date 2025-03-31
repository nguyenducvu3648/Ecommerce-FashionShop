package com.demo_crud.demo.Mapper.Order;

import com.demo_crud.demo.Enum.PaymentMethod;
import com.demo_crud.demo.dto.response.Order.OrderItemResponse;
import com.demo_crud.demo.dto.response.Order.OrderResponse;
import com.demo_crud.demo.dto.response.ProductResponse.ProductResponse;
import com.demo_crud.demo.entity.Order;
import com.demo_crud.demo.entity.OrderItem;
import com.demo_crud.demo.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "user.roles", ignore = true)
    @Mapping(target = "orderItems", source = "orderItems")
    @Mapping(target = "status", source = "orderStatus")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "availableAddresses", ignore = true)
    @Mapping(target = "totalAmount", expression = "java(order.getOrderItems() != null ? order.getOrderItems().stream().mapToDouble(OrderItem::getTotalPrice).sum() : 0.0)")
    @Mapping(target = "paymentMethod", expression = "java(order.getPayment() == null ? getAllPaymentMethods() : null)")
    OrderResponse toOrderResponse(Order order);

    default List<String> getAllPaymentMethods() {
        return Arrays.stream(PaymentMethod.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Mapping(target = "productResponse", source = "product")
    @Mapping(target = "totalPrice", source = "totalPrice")
    OrderItemResponse toOrderItemResponse(OrderItem orderItem);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "releaseDate", source = "releaseDate")
    @Mapping(target = "imageUrl", source = "imageUrl")
    ProductResponse toProductResponse(Product product);

}
