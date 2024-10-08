package com.demo_crud.demo.dto.request;


import com.demo_crud.demo.Validator.DobConstraint;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    @Size(min = 8, message = "USER_INVALID")
    String username;
    @Size(min = 8, message = "PASSWORD_INVALID")
    String password;
    String firstName;
    String lastName;
    @DobConstraint(min = 18, message = "INVALID_DOB")
    LocalDate dateOfBirth;
    List<String> roles;
}
