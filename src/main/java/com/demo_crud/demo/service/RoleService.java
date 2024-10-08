package com.demo_crud.demo.service;

import com.demo_crud.demo.Mapper.RoleMapper;
import com.demo_crud.demo.dto.request.RoleRequest;
import com.demo_crud.demo.dto.response.RoleResponse;
import com.demo_crud.demo.repository.PermissionRepository;
import com.demo_crud.demo.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;
    public RoleResponse create(RoleRequest request) {
        var role = roleMapper.toRole(request);
        if (role.getName() == null || role.getName().isEmpty()) {
            throw new IllegalArgumentException("Role name cannot be null or empty");
        }
        var permissions = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permissions));
        role = roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }
    public List<RoleResponse> getAll() {
        var roles = roleRepository.findAll();
        return  roles.stream()
                .map(roleMapper::toRoleResponse)
                .toList();
    }
    public void delete(String roles) {
        roleRepository.deleteById(roles);
    }
}
