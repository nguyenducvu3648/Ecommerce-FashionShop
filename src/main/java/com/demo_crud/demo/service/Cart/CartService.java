package com.demo_crud.demo.service.Cart;

import com.demo_crud.demo.repository.Cart.CartRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults (level = AccessLevel.PRIVATE, makeFinal = true)
public class CartService {
    CartRepository cartRepository;

    public void addProductToCart(String productId, int quantity) {

    }
}
