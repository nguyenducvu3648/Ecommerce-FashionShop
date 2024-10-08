package com.demo_crud.demo.service;

import com.demo_crud.demo.Constant.PredefinedRole;
import com.demo_crud.demo.Mapper.UserMapper;
import com.demo_crud.demo.dto.request.ApiResponse;
import com.demo_crud.demo.dto.request.UserCreationRequest;
import com.demo_crud.demo.dto.request.UserUpdateRequest;
import com.demo_crud.demo.dto.response.UserResponse;
import com.demo_crud.demo.entity.Role;
import com.demo_crud.demo.entity.User;
import com.demo_crud.demo.exception.AppException;
import com.demo_crud.demo.exception.ErrorCode;
import com.demo_crud.demo.repository.RoleRepository;
import com.demo_crud.demo.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashSet;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.List;
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    PasswordEncoder passwordEncoder;
    UserRepository userRepository;
    UserMapper userMapper;
    RoleRepository roleRepository;
    public  UserResponse createUser(UserCreationRequest request){
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById(PredefinedRole.USER_ROLE)
                .ifPresent(roles::add);
        user.setRoles(roles);
        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        return userMapper.toUserResponse(user);
    }
    public ApiResponse<List<UserResponse>> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserResponse> userResponses = users.stream()
                .map(userMapper::toUserResponse)
                .collect(Collectors.toList());
        return ApiResponse.<List<UserResponse>>builder()
                .data(userResponses)
                .build();
    }
    public ApiResponse<UserResponse> getUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        UserResponse userResponse = userMapper.toUserResponse(user);
        return ApiResponse.<UserResponse>builder()
                .code(0)
                .message(null)
                .data(userResponse)
                .build();
    }
    public UserResponse updateUser(String id , UserUpdateRequest request){
        User user = userRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED));
        userMapper.updateUser( user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));
        return userMapper.toUserResponse(userRepository.save(user));
    }
    public void deleteUser(@PathVariable UUID id){
        userRepository.deleteById(String.valueOf(id));
    }
    public ApiResponse<UserResponse> getMyInfo(){
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepository.findByUsername(name).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return ApiResponse.<UserResponse>builder()
                .data(userMapper.toUserResponse(user))
                .build();
    }
}
