package com.demo_crud.demo.Mapper.Cart;

import com.demo_crud.demo.dto.request.Cart.CartAdditionRequest;
import com.demo_crud.demo.dto.request.Cart.CartEditionRequest;
import com.demo_crud.demo.dto.response.Cart.CartResponse;
import com.demo_crud.demo.entity.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CartMapper {
    Cart toCart(CartAdditionRequest request);

    CartResponse toCartResponse(Cart cart);

    void updateCart(@MappingTarget Cart cart, CartEditionRequest request);
}
