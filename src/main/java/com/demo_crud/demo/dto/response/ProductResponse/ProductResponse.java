package com.demo_crud.demo.dto.response.ProductResponse;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {
    String id;
    String name;
    String categoryName;
    double price;
    String description;
    LocalDate releaseDate;
    String imageUrl;
}
