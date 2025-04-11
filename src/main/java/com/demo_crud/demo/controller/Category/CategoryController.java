package com.demo_crud.demo.controller.Category;

import com.demo_crud.demo.dto.request.ApiResponse;
import com.demo_crud.demo.dto.request.Category.CategoryCreationRequest;
import com.demo_crud.demo.dto.request.Category.CategoryUpdateRequest;
import com.demo_crud.demo.dto.response.Category.CategoryResponse;
import com.demo_crud.demo.service.Category.CategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
@FieldDefaults (level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController {
    CategoryService categoryService;
    @GetMapping("/getAllCategory")
    public ApiResponse<List<CategoryResponse>> getAllCategory() {
        ApiResponse<List<CategoryResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(categoryService.getAllCategory());
        return apiResponse;
    }
    @PostMapping("/createCategory")
    public ApiResponse<CategoryResponse> createCategory(@RequestBody CategoryCreationRequest request){
        ApiResponse<CategoryResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(categoryService.createCategory(request));
        return apiResponse;
    }
    @PutMapping("/updateCategory/{id}")
    public ApiResponse<CategoryResponse> updateCategory(@PathVariable String id, @RequestBody  CategoryUpdateRequest request){
        ApiResponse<CategoryResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(categoryService.updateCategory(id, request));
        return apiResponse;
    }
    @DeleteMapping("/deleteCategory/{id}")
    public void deleteCategory(@PathVariable String id) {
        categoryService.deleteCategory(id);
    }
}
