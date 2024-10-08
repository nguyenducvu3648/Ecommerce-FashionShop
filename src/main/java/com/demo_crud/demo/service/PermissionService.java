package com.demo_crud.demo.service;

import com.demo_crud.demo.Mapper.PermissionMapper;
import com.demo_crud.demo.dto.request.PermissionRequest;
import com.demo_crud.demo.dto.response.PermissionResponse;
import com.demo_crud.demo.entity.Permission;
import com.demo_crud.demo.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults( level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;
    public PermissionResponse create(PermissionRequest request) {
        Permission permission = permissionMapper.toPermission(request);
        permission = permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }
    public List<PermissionResponse> findAll() {
        List<Permission> permissions = permissionRepository.findAll();
        return permissions.stream().map(permissionMapper::toPermissionResponse).toList();
    }
    public void delete(String permission) {
        permissionRepository.deleteById(permission);
    }
}
