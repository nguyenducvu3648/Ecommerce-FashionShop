package com.demo_crud.demo.Mapper.Category;

import com.demo_crud.demo.dto.request.Category.CategoryCreationRequest;
import com.demo_crud.demo.dto.response.Category.CategoryResponse;
import com.demo_crud.demo.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryResponse toCategoryResponse(Category category);

    Category toCategory(CategoryCreationRequest request);
}
