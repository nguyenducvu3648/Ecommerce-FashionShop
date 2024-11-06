package com.demo_crud.demo.controller;

import com.demo_crud.demo.dto.request.ApiResponse;
import com.demo_crud.demo.dto.request.UserCreationRequest;
import com.demo_crud.demo.dto.request.UserUpdateRequest;
import com.demo_crud.demo.dto.response.UserResponse;
import com.demo_crud.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/users")
@Tag(name = "Quản lý User")
public class UserController {
    UserService userService;

    @Operation(summary = "tat ca user")
    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")//kiem tra vai tro
//    @PreAuthorize("hasAuthority('APROVE_POST')")   // kiem tra quyen truoc khi chay
    public ApiResponse<List<UserResponse>> getAllUsers() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Username:{}", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));
        return userService.getAllUsers();
    }
    @Operation(summary = "dang ki")
    @PostMapping("/sign-up")
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(userService.createUser(request));
        return apiResponse;
    }
    @Operation(summary = "tim kiem user")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<UserResponse> getUserById(@PathVariable String id) {
        return userService.getUserById(id);
    }
    //@PreAuthorize kiem tra truoc khi chay funcion
    //@PostAuthorize chay funcion roi kiem tra neu dung thi tra ket qua
    @Operation(summary = "cap nhat user")
    @PutMapping("/{id}")
    @PostAuthorize("returnObject.data.username == authentication.name")
    ApiResponse<UserResponse> updateUser(@PathVariable String id, @RequestBody @Valid UserUpdateRequest request) {
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(userService.updateUser(id, request));
        return apiResponse;
    }
    @Operation(summary = "xoa user")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    String deleteUser(@PathVariable String id) {
         userService.deleteUser(UUID.fromString(id));
         return "User deleted";
    }
    @Operation(summary = "thong tin user")
    @GetMapping("/myInfo")
    @PostAuthorize("returnObject.data.username == authentication.name")//user chi co the lay thong tin cua chinh minh ma thoi
    ApiResponse<UserResponse> getMyInfo(){
        return userService.getMyInfo();
    }
}
