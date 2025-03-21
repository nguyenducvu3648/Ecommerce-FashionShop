package com.demo_crud.demo.dto.response;


import com.demo_crud.demo.dto.response.Address.AddressResponse;
import com.demo_crud.demo.entity.Address;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String username;
    String firstName;
    String lastName;
    LocalDate dob;
    Set<RoleResponse> roles;
    Set<AddressResponse> addresses;
}
