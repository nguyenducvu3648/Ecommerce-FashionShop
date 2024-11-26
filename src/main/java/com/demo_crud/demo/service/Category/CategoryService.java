package com.demo_crud.demo.service.Category;

import com.demo_crud.demo.Mapper.Category.CategoryMapper;
import com.demo_crud.demo.dto.request.Category.CategoryCreationRequest;
import com.demo_crud.demo.dto.request.Category.CategoryUpdateRequest;
import com.demo_crud.demo.dto.response.Category.CategoryResponse;
import com.demo_crud.demo.entity.Category;
import com.demo_crud.demo.exception.AppException;
import com.demo_crud.demo.exception.ErrorCode;
import com.demo_crud.demo.repository.Category.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService {
    CategoryRepository categoryRepository;
    CategoryMapper categoryMapper;
    @Transactional
    public CategoryResponse createCategory(CategoryCreationRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.CATEGORY_EXISTED);
        }
        Category category = categoryMapper.toCategory(request);

        categoryRepository.save(category);
        return categoryMapper.toCategoryResponse(category);
    }
    public CategoryResponse updateCategory( String id, CategoryUpdateRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
        categoryMapper.updateCategory(category, request);
        return categoryMapper.toCategoryResponse(categoryRepository.save(category));
    }
    public void deleteCategory(String id) {
        categoryRepository.deleteById(id);
    }
}
