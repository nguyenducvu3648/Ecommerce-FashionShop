package com.demo_crud.demo.Mapper;

import com.demo_crud.demo.dto.request.RoleRequest;
import com.demo_crud.demo.dto.response.RoleResponse;
import com.demo_crud.demo.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true )
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
