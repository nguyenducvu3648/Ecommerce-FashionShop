package com.demo_crud.demo.dto.request.ProductRequest;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductUpdateRequest {
    String name;
    double price;
    String description;
    LocalDate releaseDate;
    String imageUrl;
    int quantity;
}
