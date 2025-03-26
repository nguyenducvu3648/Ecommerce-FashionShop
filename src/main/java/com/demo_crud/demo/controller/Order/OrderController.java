package com.demo_crud.demo.controller.Order;

import com.demo_crud.demo.dto.request.ApiResponse;
import com.demo_crud.demo.dto.response.Order.OrderResponse;
import com.demo_crud.demo.service.Order.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class OrderController {
    OrderService orderService;

    @PostMapping("/createOrder")
    public ApiResponse<OrderResponse> createOrder(@RequestBody List<String> cartItemIds) {
        ApiResponse<OrderResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(orderService.createOrder(cartItemIds));
        return apiResponse;
    }
}
