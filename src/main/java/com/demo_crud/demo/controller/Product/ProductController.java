package com.demo_crud.demo.controller.Product;

import com.demo_crud.demo.dto.request.ApiResponse;
import com.demo_crud.demo.dto.request.ProductRequest.ProductCreationRequest;
import com.demo_crud.demo.dto.request.ProductRequest.ProductUpdateRequest;
import com.demo_crud.demo.dto.response.ProductResponse.ProductResponse;
import com.demo_crud.demo.service.Product.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.coyote.BadRequestException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/product")
@Tag(name = "Quản lý Sản phẩm")
public class ProductController {
    ProductService productService ;
    ObjectMapper objectMapper;

    @Operation(summary = "Lấy tất cả sản phẩm")
    @GetMapping("/getAll")
    public ApiResponse<List<ProductResponse>> getAll(){
        return productService.getAllProducts();
    }
    @Operation(summary = "Lấy sản phẩm theo ID")
    @GetMapping("/{id}")
    public ApiResponse<ProductResponse> getProductById(@PathVariable String id){
        return productService.getProductById(id);
    }

    @Operation(summary = "Tạo sản phẩm mới")
    @PostMapping(value = "/createProduct",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ApiResponse<ProductResponse> createProduct(
            @RequestPart(value = "file", required = false) MultipartFile imageFile,
            @RequestPart("request") String requestJson) throws IOException {

        if (requestJson == null || requestJson.isEmpty()) {
            throw new BadRequestException("Request payload cannot be empty");
        }

        ProductCreationRequest request;
        try {
            request = objectMapper.readValue(requestJson, ProductCreationRequest.class);
        } catch (JsonProcessingException e) {
            throw new BadRequestException("Định dạng yêu cầu không hợp lệ: " + e.getMessage());
        }
        ProductResponse productResponse = productService.createProduct(request, imageFile);
        ApiResponse<ProductResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(productResponse);
        return apiResponse;
    }


    @Operation(summary = "Cập nhật sản phẩm hiện có")
    @PutMapping("/updateProduct/{id}")
    public ApiResponse<ProductResponse> updateProduct(@PathVariable String id,
                                                      @RequestPart(value = "file", required = false) MultipartFile imageFile,
                                                      @RequestBody ProductUpdateRequest request) {
        ApiResponse<ProductResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(productService.updateProduct(id, request, imageFile));
        return apiResponse;
    }

    @Operation(summary = "Xóa sản phẩm")
    @DeleteMapping("/deleteProduct/{id}")
    public String deleteProduct(@PathVariable String id){
        productService.deleteProduct(String.valueOf(UUID.fromString(id)));
        return "product has been deleted";
    }
    @GetMapping("/getProductByCategory/{id}")
    public ApiResponse<List<ProductResponse>> getProductByCategory( @PathVariable String id){
        ApiResponse<List<ProductResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(productService.getProductByCategory(id));
        return apiResponse;
    }
    @GetMapping("/searchProductByKeyword")
    public ApiResponse<List<ProductResponse>> searchProductByKeyword(@RequestParam String keyword){
        ApiResponse<List<ProductResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(productService.searchProductByKeyword(keyword));
        return apiResponse;
    }
}
