package com.demo_crud.demo.controller;

import com.demo_crud.demo.dto.request.ApiResponse;
import com.demo_crud.demo.dto.request.PermissionRequest;
import com.demo_crud.demo.dto.response.PermissionResponse;
import com.demo_crud.demo.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permission")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
    PermissionService permissionService;
    @PostMapping("/createPermission")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<PermissionResponse> createPermission(@RequestBody PermissionRequest request) {
        return ApiResponse.<PermissionResponse>builder()
                .data(permissionService.create(request))
                .build();
    }
    @GetMapping("/allPermission")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<List<PermissionResponse>> findAllPermissions() {
        return ApiResponse.<List<PermissionResponse>>builder()
                .data(permissionService.findAll())
                .build();
    }
    @DeleteMapping("/{permissions}")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<Void> deletePermission(@PathVariable("permissions") String permission) {
        permissionService.delete(permission);
        return ApiResponse.<Void>builder().build();
    }
}
