package com.demo_crud.demo.controller.Category;

import com.demo_crud.demo.dto.request.ApiResponse;
import com.demo_crud.demo.dto.request.Category.CategoryCreationRequest;
import com.demo_crud.demo.dto.response.Category.CategoryResponse;
import com.demo_crud.demo.service.Category.CategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
@FieldDefaults (level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController {
    CategoryService categoryService;
    @PostMapping("/createCategory")
    public ApiResponse<CategoryResponse> createCategory(CategoryCreationRequest request){
        ApiResponse<CategoryResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(categoryService.createCategory(request));
        return apiResponse;
    }
}
