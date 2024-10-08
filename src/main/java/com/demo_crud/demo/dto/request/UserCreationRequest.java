package com.demo_crud.demo.dto.request;

import com.demo_crud.demo.Validator.DobConstraint;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@NoArgsConstructor //Tao constructor khong tham so
@AllArgsConstructor //tao constructor co tham so
@Getter
@Setter// tao cac phuong thuc getter va setter
@Builder // giup tao cac object nhanh hon ma khong can day du cac tham so
@FieldDefaults(level = AccessLevel.PRIVATE) // cac truong ma khong co modifiler thi mac dinh se la private
public class UserCreationRequest {
    @Size(min = 8, message = "USER_INVALID")
    String username;
    @Size(min = 8, message = "PASSWORD_INVALID")
    String password;
    String firstName;
    String lastName;
    @DobConstraint(min = 18, message = "INVALID_DOB")
    LocalDate dob;
}
