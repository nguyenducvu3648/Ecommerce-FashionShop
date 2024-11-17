package com.demo_crud.demo.controller.Cart;

import com.demo_crud.demo.dto.request.ApiResponse;
import com.demo_crud.demo.dto.request.Cart.CartAdditionRequest;
import com.demo_crud.demo.dto.request.Cart.CartEditionRequest;
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

    @PostMapping("/addProductToCart")
    public ApiResponse<CartResponse> addProductToCart(@RequestBody CartAdditionRequest request) {
        ApiResponse<CartResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(cartService.addProductToCart(request));
        return apiResponse;
    }
    @PostMapping("/editCart")
    public ApiResponse<CartResponse> editCart(@RequestBody CartEditionRequest request){
        ApiResponse<CartResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(cartService.editCart(request));
        return apiResponse;
    }

    @GetMapping("/viewCart")
    public ApiResponse<CartResponse> viewCart() {
        return cartService.viewCart();
    }

    @PostMapping("/checkout")
    public ApiResponse<CartResponse> checkout() {
        ApiResponse<CartResponse> response = new ApiResponse<>();
        try {
            CartResponse cartResponse = cartService.checkout();
            response.setData(cartResponse);
            response.setMessage("Checkout successful!");
        } catch (Exception e) {
            response.setMessage("Error: " + e.getMessage());
        }
        return response;
    }
}
