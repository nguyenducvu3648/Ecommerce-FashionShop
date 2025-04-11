package com.demo_crud.demo.Mapper.Category;

import com.demo_crud.demo.dto.request.Category.CategoryCreationRequest;
import com.demo_crud.demo.dto.request.Category.CategoryUpdateRequest;
import com.demo_crud.demo.dto.response.Category.CategoryResponse;
import com.demo_crud.demo.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryResponse toCategoryResponse(Category category);

    Category toCategory(CategoryCreationRequest request);

    void updateCategory(@MappingTarget Category category, CategoryUpdateRequest request);
}
