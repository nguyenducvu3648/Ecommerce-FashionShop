package com.demo_crud.demo.service.Cart;

import com.demo_crud.demo.Mapper.Cart.CartMapper;
import com.demo_crud.demo.dto.request.Cart.CartAdditionRequest;
import com.demo_crud.demo.dto.request.Cart.CartEditionRequest;
import com.demo_crud.demo.entity.Cart;
import com.demo_crud.demo.entity.CartItem;
import com.demo_crud.demo.entity.Product;
import com.demo_crud.demo.entity.User;
import com.demo_crud.demo.exception.AppException;
import com.demo_crud.demo.exception.ErrorCode;
import com.demo_crud.demo.repository.Cart.CartRepository;
import com.demo_crud.demo.repository.CartItem.CartItemRepository;
import com.demo_crud.demo.repository.Product.ProductRepository;
import com.demo_crud.demo.dto.response.Cart.CartResponse;
import com.demo_crud.demo.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartService {
    CartRepository cartRepository;
    ProductRepository productRepository;
    CartMapper cartMapper;
    CartItemRepository cartItemRepository;
    UserService userService;

    public CartResponse addProductToCart(CartAdditionRequest request) {
        Product product = productRepository.findById(String.valueOf(request.getProductId()))
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        if (product.getQuantity() < request.getQuantity()) {
            throw new AppException(ErrorCode.INSUFFICIENT_STOCK);
        }

        User currentUser = userService.getCurrentUser();
        Cart cart = cartRepository.findByUser(currentUser)
                .orElseGet(() -> createCartForUser(currentUser));  // Nếu chưa có giỏ hàng, tạo mới

        // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
        CartItem existingCartItem = findCartItem(cart, product);
        if (existingCartItem != null) {
            // Nếu đã có, cập nhật số lượng
            int newQuantity = existingCartItem.getQuantity() + request.getQuantity();
            if (product.getQuantity() < newQuantity) {
                throw new AppException(ErrorCode.INSUFFICIENT_STOCK);
            }
            existingCartItem.setQuantity(newQuantity);
            existingCartItem.setTotalPrice(existingCartItem.getQuantity() * product.getPrice());
        } else {
            // Nếu chưa có, tạo CartItem mới
            CartItem newCartItem = new CartItem();
            newCartItem.setCart(cart);
            newCartItem.setProduct(product);
            newCartItem.setQuantity(request.getQuantity());
            newCartItem.setTotalPrice(request.getQuantity() * product.getPrice());
            cart.getCartItems().add(newCartItem);
        }
        double totalCartPrice = cart.getCartItems().stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
        cart.setTotalCartPrice(totalCartPrice);

        cartRepository.save(cart);
        log.info("Cart after update: {}", cart);
        log.info("Cart items: {}", cart.getCartItems());

        return cartMapper.toCartResponse(cart);
    }

    // Tìm CartItem trong giỏ hàng theo sản phẩm
    private CartItem findCartItem(Cart cart, Product product) {
        return cart.getCartItems().stream()
                .filter(cartItem -> cartItem.getProduct().equals(product))
                .findFirst()
                .orElse(null);
    }
    // Tạo giỏ hàng mới cho người dùng (nếu chưa có)
    private Cart createCartForUser(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setCartItems(new ArrayList<>());
        return cartRepository.save(cart);
    }





    public CartResponse viewCart() {
        User currentUser = userService.getCurrentUser();
        Cart cart = cartRepository.findByUser(currentUser)
                .orElseGet(() -> {
                    log.info("No cart found for user {}. Returning an empty cart.", currentUser.getUsername());
                    return new Cart(); // Trả về giỏ hàng rỗng
                });
        return cartMapper.toCartResponse(cart);
    }

    public CartResponse editCart(CartEditionRequest request) {
        User currentUser = userService.getCurrentUser();
        Cart cart = cartRepository.findByUser(currentUser)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_EXISTED));

        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getId().equals(request.getCartItemId()))
                .findFirst()
                .orElseThrow(() -> new AppException(ErrorCode.CART_ITEM_NOT_EXISTED));

        Product product = cartItem.getProduct();
        if (request.getQuantity() == 0) {
            cart.getCartItems().remove(cartItem);
        } else {
            if (product.getQuantity() < request.getQuantity()) {
                throw new AppException(ErrorCode.INSUFFICIENT_STOCK);
            }
            cartItem.setQuantity(request.getQuantity());
            cartItem.setTotalPrice(request.getQuantity() * product.getPrice());
        }

        double totalCartPrice = cart.getCartItems().stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
        cart.setTotalCartPrice(totalCartPrice);

        cartRepository.save(cart);
        return cartMapper.toCartResponse(cart);
    }

    public CartResponse removeCartItem(String cartItemId) {
        User currentUser = userService.getCurrentUser();

        Cart cart = cartRepository.findByUser(currentUser)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_EXISTED));
        log.info("Cart items for user {}: {}", currentUser.getUsername(), cart.getCartItems());
        log.info("Removing cart item with id: {}", cartItemId);

        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new AppException(ErrorCode.CART_ITEM_NOT_EXISTED));
        cart.getCartItems().remove(cartItem);


        double totalCartPrice = cart.getCartItems().stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
        cart.setTotalCartPrice(totalCartPrice);

        cartRepository.save(cart);
        return cartMapper.toCartResponse(cart);
    }

    public CartResponse clearCart() {
        User currentUser = userService.getCurrentUser();
        Cart cart = cartRepository.findByUser(currentUser)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_EXISTED));

        cart.getCartItems().clear();
        cart.setTotalCartPrice(0);

        cartRepository.save(cart);
        return cartMapper.toCartResponse(cart);
    }

    @Transactional
    public List<CartItem> selectCartItems(List<String> cartItemIds) {
        User currentUser = userService.getCurrentUser();
        Cart cart = cartRepository.findByUser(currentUser)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_EXISTED));

        List<CartItem> updatedCartItems = cart.getCartItems().stream()
                .filter(cartItem -> cartItemIds.contains(cartItem.getId()))
                .peek(cartItem -> cartItem.setSelected(true))
                .collect(Collectors.toList());

        cartItemRepository.saveAll(updatedCartItems);
        return updatedCartItems;
    }

}
