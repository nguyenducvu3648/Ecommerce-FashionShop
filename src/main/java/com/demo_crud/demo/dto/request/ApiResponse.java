package com.demo_crud.demo.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PACKAGE)
public class ApiResponse <T>{
    int code = 1000;
    String message;
    T data;
}
