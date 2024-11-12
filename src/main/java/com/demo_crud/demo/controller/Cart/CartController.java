package com.demo_crud.demo.controller.Cart;

import com.demo_crud.demo.dto.request.ApiResponse;
import com.demo_crud.demo.dto.request.Cart.CartAdditionRequest;
import com.demo_crud.demo.dto.response.Cart.CartResponse;
import com.demo_crud.demo.service.Cart.CartService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/cart")
public class CartController {
    CartService cartService;

    @PostMapping("/addProduct")
    ApiResponse<CartResponse> addProduct(@RequestBody CartAdditionRequest request) {
        ApiResponse<CartResponse> response = new ApiResponse<>();
        return response;
    }


    @GetMapping("/viewCart")
    public ApiResponse<CartResponse> viewCart() {
        // Logic to view cart
        return new ApiResponse<CartResponse>();
    }

    @PostMapping("/checkout")
    public ApiResponse<CartResponse> checkout() {
        // Logic to checkout
        return new ApiResponse<CartResponse>();
    }
}
