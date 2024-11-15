package com.demo_crud.demo.service.Cart;

import com.demo_crud.demo.dto.request.Cart.CartAdditionRequest;
import com.demo_crud.demo.entity.Cart;
import com.demo_crud.demo.entity.Product;
import com.demo_crud.demo.exception.AppException;
import com.demo_crud.demo.exception.ErrorCode;
import com.demo_crud.demo.repository.Cart.CartRepository;
import com.demo_crud.demo.repository.Product.ProductRepository;
import com.demo_crud.demo.dto.response.Cart.CartProduct;
import com.demo_crud.demo.dto.response.Cart.CartResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartService {
    CartRepository cartRepository;
    ProductRepository productRepository;

    public CartResponse addProductToCart(CartAdditionRequest request) {
        Optional<Product> productOpt = productRepository.findById(request.getProductId());
        if (!productOpt.isEmpty()) {
            throw new AppException(ErrorCode.PRODUCT_NOT_EXISTED);
        }

        Product product = productOpt.get();

        Cart cart = cartRepository.findFirstByOrderByIdDesc().orElse(new Cart());

        Optional<Product> existingProductOpt = cart.getProducts().stream()
                .filter(p -> p.getId().equals(request.getProductId()))
                .findFirst();

        if (existingProductOpt.isPresent()) {
            Product existingProduct = existingProductOpt.get();
            existingProduct.setQuantity(existingProduct.getQuantity() + request.getQuantity());
        } else {
            product.setQuantity(request.getQuantity());
            cart.getProducts().add(product);
        }

        double totalAmount = cart.getProducts().stream()
                .mapToDouble(p -> p.getPrice() * p.getQuantity())
                .sum();

        cartRepository.save(cart);

        // Tạo response trả về
        List<CartProduct> cartProducts = cart.getProducts().stream()
                .map(p -> new CartProduct(p.getId(), p.getName(), p.getPrice(), p.getQuantity(), p.getPrice() * p.getQuantity()))
                .toList();

        return new CartResponse(cartProducts, totalAmount);
    }

    // Lấy thông tin giỏ hàng
    public CartResponse viewCart() {
        Cart cart = cartRepository.findFirstByOrderByIdDesc()
                .orElseThrow(() -> new RuntimeException("Cart is empty!"));

        List<CartProduct> cartProducts = cart.getProducts().stream()
                .map(p -> new CartProduct(p.getId(), p.getName(), p.getPrice(), p.getQuantity(), p.getPrice() * p.getQuantity()))
                .toList();

        double totalAmount = cart.getProducts().stream()
                .mapToDouble(p -> p.getPrice() * p.getQuantity())
                .sum();

        return new CartResponse(cartProducts, totalAmount);
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
