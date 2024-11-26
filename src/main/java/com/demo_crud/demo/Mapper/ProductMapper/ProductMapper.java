package com.demo_crud.demo.Mapper.ProductMapper;

import com.demo_crud.demo.dto.request.ProductRequest.ProductCreationRequest;
import com.demo_crud.demo.dto.request.ProductRequest.ProductUpdateRequest;
import com.demo_crud.demo.dto.response.ProductResponse.ProductResponse;
import com.demo_crud.demo.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {


    @Mapping(target = "category", ignore = true)
    Product toProduct(ProductCreationRequest request);

    @Mapping(target = "categoryName", expression = "java(product.getCategory() != null ? product.getCategory().getName() : null)")
    ProductResponse toProductResponse(Product product);

    void updateProduct(@MappingTarget Product product, ProductUpdateRequest request);
}
