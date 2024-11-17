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
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartService {
    CartRepository cartRepository;
    ProductRepository productRepository;
    CartMapper cartMapper;
    UserRepository userRepository;

    @Transactional
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

        cartRepository.save(cart);
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

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            // Nếu principal là một UserDetails, bạn có thể lấy username hoặc ID người dùng
            if (principal instanceof UserDetails userDetails) {
                String username = userDetails.getUsername();  // Hoặc bạn có thể lấy ID người dùng tùy theo cách cấu trúc UserDetails

                // Giả sử bạn có phương thức để tìm người dùng qua username hoặc ID
                return userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
            } else {
                throw new RuntimeException("User is not authenticated properly");
            }
        } else {
            throw new RuntimeException("No authenticated user found in SecurityContext");
        }
    }


    public CartResponse editCart(CartEditionRequest request){
        Product product = productRepository.findById(request.getCartItemId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        return null;
    }

    public ApiResponse<CartResponse> viewCart() {
        Cart cart = cartRepository.findFirstByOrderByIdDesc()
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_EXISTED));
        return ApiResponse.<CartResponse>builder()
                .data(cartMapper.toCartResponse(cart))
                .build();
    }

    // Thực hiện thanh toán
    public CartResponse checkout() {
        Cart cart = cartRepository.findFirstByOrderByIdDesc()
                .orElseThrow(() -> new RuntimeException("Cart is empty!"));

        // Xử lý thanh toán logic ở đây (giả sử xử lý đơn giản)

        // Sau khi thanh toán, xóa giỏ hàng
        cartRepository.delete(cart);

        // Tạo response sau khi thanh toán
        return new CartResponse(List.of(), 0.0);
    }
}
