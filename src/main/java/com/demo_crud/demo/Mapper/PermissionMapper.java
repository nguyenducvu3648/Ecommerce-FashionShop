package com.demo_crud.demo.Mapper;

import com.demo_crud.demo.dto.request.PermissionRequest;
import com.demo_crud.demo.dto.response.PermissionResponse;
import com.demo_crud.demo.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);
    PermissionResponse toPermissionResponse(Permission permission);
}