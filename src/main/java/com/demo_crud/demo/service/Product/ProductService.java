package com.demo_crud.demo.service.Product;

import com.demo_crud.demo.Mapper.ProductMapper.ProductMapper;
import com.demo_crud.demo.dto.request.ApiResponse;
import com.demo_crud.demo.dto.request.ProductRequest.ProductCreationRequest;
import com.demo_crud.demo.dto.request.ProductRequest.ProductUpdateRequest;
import com.demo_crud.demo.dto.response.ProductResponse.ProductResponse;
import com.demo_crud.demo.entity.Category;
import com.demo_crud.demo.entity.Product;
import com.demo_crud.demo.exception.AppException;
import com.demo_crud.demo.exception.ErrorCode;
import com.demo_crud.demo.repository.Category.CategoryRepository;
import com.demo_crud.demo.repository.Product.ProductRepository;
import com.demo_crud.demo.service.Cloudinary.CloudinaryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductService {
    ProductRepository productRepository;
    ProductMapper productMapper;
    CategoryRepository categoryRepository;
    CloudinaryService cloudinaryService;

    public ApiResponse<List<ProductResponse>> getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductResponse>productResponses = products.stream()
                .map(productMapper :: toProductResponse)
                .collect(Collectors.toList());
        return ApiResponse.<List<ProductResponse>>builder()
                .data(productResponses)
                .build();
    }
    public ProductResponse createProduct(ProductCreationRequest request,
                                         MultipartFile imageFile) {
        if (productRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.PRODUCT_EXISTED);
        }
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));

        String imageUrl = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            imageUrl = cloudinaryService.uploadImage(imageFile, "products");
        }

        Product product = productMapper.toProduct(request);
        product.setCategory(category);
        product.setImageUrl(imageUrl);
        product = productRepository.save(product);
        return productMapper.toProductResponse(product);
    }

    public ProductResponse updateProduct(String id , ProductUpdateRequest request,
                                         MultipartFile imageFile) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
        if (imageFile != null) {
            String imageUrl = cloudinaryService.uploadImage(imageFile, "products");
            product.setImageUrl(imageUrl);
        }
        productMapper.updateProduct(product, request);
        return productMapper.toProductResponse(productRepository.save(product));
    }

    public ApiResponse<ProductResponse> getProductById(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
        ProductResponse productResponse = productMapper.toProductResponse(product);
        return ApiResponse.<ProductResponse>builder()
                .data(productResponse)
                .build();
    }

    public void deleteProduct(String id) {
        productRepository.deleteById(id);
    }

    public List<ProductResponse> getProductByCategory(String id) {
        List<Product> products = productRepository.findByCategoryId(id);
        return products.stream()
                .map(productMapper::toProductResponse)
                .collect(Collectors.toList());
    }
    public List<ProductResponse> searchProductByKeyword(String keyword) {
        List<Product> products = productRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword);
        if (products.isEmpty()) {
            throw new AppException(ErrorCode.PRODUCT_NOT_EXISTED);
        }
        return products.stream()
                .map(productMapper::toProductResponse)
                .collect(Collectors.toList());
    }
}
