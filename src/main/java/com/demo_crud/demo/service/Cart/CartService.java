package com.demo_crud.demo.service.Cart;

import com.demo_crud.demo.Mapper.Cart.CartMapper;
import com.demo_crud.demo.dto.request.ApiResponse;
import com.demo_crud.demo.dto.request.Cart.CartAdditionRequest;
import com.demo_crud.demo.dto.request.Cart.CartEditionRequest;
import com.demo_crud.demo.entity.Cart;
import com.demo_crud.demo.entity.CartItem;
import com.demo_crud.demo.entity.Product;
import com.demo_crud.demo.entity.User;
import com.demo_crud.demo.exception.AppException;
import com.demo_crud.demo.exception.ErrorCode;
import com.demo_crud.demo.repository.Cart.CartRepository;
import com.demo_crud.demo.repository.Product.ProductRepository;
import com.demo_crud.demo.dto.response.Cart.CartResponse;
import com.demo_crud.demo.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartService {
    CartRepository cartRepository;
    ProductRepository productRepository;
    CartMapper cartMapper;
    UserRepository userRepository;

    public CartResponse addProductToCart(CartAdditionRequest request) {
        Product product = productRepository.findById(String.valueOf(request.getProductId()))
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        if (product.getQuantity() < request.getQuantity()) {
            throw new AppException(ErrorCode.INSUFFICIENT_STOCK);
        }

        // Lấy giỏ hàng của người dùng
        User currentUser = getCurrentUser();  // Phương thức này sẽ lấy thông tin người dùng hiện tại
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

    // Lấy người dùng hiện tại (thực tế có thể lấy từ SecurityContext hoặc Session)
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Authentication: {}", authentication);

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            // Xử lý Jwt principal
            if (principal instanceof Jwt jwt) {
                // Lấy username từ JWT claims
                String username = jwt.getClaimAsString("sub"); // hoặc "preferred_username" tùy cấu hình
                // Hoặc có thể lấy email: jwt.getClaimAsString("email")

                log.info("Username from JWT: {}", username);

                return userRepository.findByUsername(username)
                        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
            } else if (principal instanceof UserDetails userDetails) {
                String username = userDetails.getUsername();
                return userRepository.findByUsername(username)
                        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
            }

            log.error("Unexpected principal type: {}", principal.getClass().getName());
        }
        throw new AppException(ErrorCode.UNAUTHORIZED_ACCESS);
    }




    public ApiResponse<CartResponse> viewCart() {
        User currentUser = getCurrentUser();
        log.info("Getting cart for user: {}", currentUser.getUsername());

        // Tìm giỏ hàng của user
        Optional<Cart> existingCart = cartRepository.findByUser(currentUser);

        if (existingCart.isPresent()) {
            log.info("Found existing cart for user {}", currentUser.getUsername());
        }
        return ApiResponse.<CartResponse>builder()
                .data(cartMapper.toCartResponse(existingCart.get()))
                .build();
    }

    public CartResponse editCart(CartEditionRequest request) {
        User currentUser = getCurrentUser();
        Cart cart = cartRepository.findByUser(currentUser)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_EXISTED));

        // Tìm sản phẩm trong giỏ hàng
        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getId().equals(request.getCartItemId()))
                .findFirst()
                .orElseThrow(() -> new AppException(ErrorCode.CART_ITEM_NOT_EXISTED));
        if (!cartItem.getProduct().getId().equals(request.getProductId())) {
            throw new AppException(ErrorCode.PRODUCT_NOT_EXISTED); // Nếu không khớp, ném ra lỗi
        }
        switch (request.getAction()) {
            case "REMOVE" ->
                // Xóa sản phẩm khỏi giỏ hàng nếu hành động là REMOVE
                    cart.getCartItems().remove(cartItem);
            case "UPDATE" -> {
                // Kiểm tra tồn kho khi cập nhật số lượng
                Product product = cartItem.getProduct();
                if (request.getQuantity() < 0 || request.getQuantity() > product.getQuantity()) {
                    // Nếu số lượng yêu cầu lớn hơn số lượng tồn kho hoặc nhỏ hơn 0
                    throw new AppException(ErrorCode.INSUFFICIENT_STOCK);
                }

                // Cập nhật số lượng và tổng giá
                cartItem.setQuantity(request.getQuantity());
                cartItem.setTotalPrice(request.getQuantity() * product.getPrice());
            }
            case "ADD" -> {
                // Thêm sản phẩm vào giỏ hàng nếu hành động là ADD
                Product product = productRepository.findById((request.getProductId()))
                        .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

                // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
                CartItem newCartItem = new CartItem();
                newCartItem.setProduct(product);
                newCartItem.setQuantity(request.getQuantity());
                newCartItem.setTotalPrice(request.getQuantity() * product.getPrice());

                // Thêm vào giỏ hàng
                cart.getCartItems().add(newCartItem);
            }
            case "DELETE_ALL" ->
                // Xóa tất cả sản phẩm trong giỏ hàng nếu hành động là DELETE_ALL
                    cart.getCartItems().clear();
            case null, default -> throw new AppException(ErrorCode.INVALID_ACTION);
        }

        // Cập nhật tổng giá giỏ hàng
        double totalCartPrice = cart.getCartItems().stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
        cart.setTotalCartPrice(totalCartPrice);


        cartRepository.save(cart);
        return cartMapper.toCartResponse(cart);
    }



    // Thực hiện thanh toán
//    public CartResponse checkout() {
//        Cart cart = cartRepository.findFirstByOrderByIdDesc()
//                .orElseThrow(() -> new RuntimeException("Cart is empty!"));
//
//        // Xử lý thanh toán logic ở đây (giả sử xử lý đơn giản)
//
//        // Sau khi thanh toán, xóa giỏ hàng
//        cartRepository.delete(cart);
//
//        // Tạo response sau khi thanh toán
//        return new CartResponse(List.of(), 0.0);
//    }
}
