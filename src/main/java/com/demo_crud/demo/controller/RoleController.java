package com.demo_crud.demo.controller;

import com.demo_crud.demo.dto.request.ApiResponse;
import com.demo_crud.demo.dto.request.RoleRequest;
import com.demo_crud.demo.dto.response.RoleResponse;
import com.demo_crud.demo.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleController {
    RoleService roleService;
    @GetMapping("/allRole")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<RoleResponse>> getAllRoles() {
        return ApiResponse.<List<RoleResponse>>builder()
                .data(roleService.getAll())
                .build();
    }
    @PostMapping("/createRole")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<RoleResponse> createRole(@RequestBody RoleRequest request) {
        return ApiResponse.<RoleResponse>builder()
                .data(roleService.create(request))
                .build();
    }
    @DeleteMapping("/{role}")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<Void> deleteRole(@PathVariable String role) {
        roleService.delete(role);
        return ApiResponse.<Void>builder().build();
    }
}
