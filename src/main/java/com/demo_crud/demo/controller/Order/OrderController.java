package com.demo_crud.demo.controller.Order;

import com.demo_crud.demo.dto.request.ApiResponse;
import com.demo_crud.demo.dto.request.Order.OrderUpdateRequest;
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
    @PutMapping("/updateOrder/{id}")
    public ApiResponse<OrderResponse> updateOrder(@PathVariable String id,@RequestBody OrderUpdateRequest request) {
        ApiResponse<OrderResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(orderService.updateOrder(id, request));
        return apiResponse;
    }
    @PutMapping("/{id}/confirm")
    public ApiResponse<OrderResponse> confirmOrder(@PathVariable String id) {
        ApiResponse<OrderResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(orderService.confirmOrder(id));
        return apiResponse;
    }
    @PutMapping("/{id}/cancel")
    public ApiResponse<OrderResponse> cancelOrder(@PathVariable String id) {
        ApiResponse<OrderResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(orderService.cancelOrder(id));
        return apiResponse;
    }

    @DeleteMapping("/{id}/deleteOrder")
    public void deleteOrder(@PathVariable String id) {
       orderService.deleteOrder(id);
    }

    @GetMapping("/getMyOrder")
    public ApiResponse<List<OrderResponse>> getMyOrder() {
        ApiResponse<List<OrderResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(orderService.getMyOrder());
        return apiResponse;
    }
}
