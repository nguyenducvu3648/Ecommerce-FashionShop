
package com.crud_demo.demo.service;

import com.demo_crud.demo.Constant.PredefinedRole;
import com.demo_crud.demo.DemoApplication;
import com.demo_crud.demo.dto.request.UserCreationRequest;
import com.demo_crud.demo.dto.request.UserUpdateRequest;
import com.demo_crud.demo.dto.response.UserResponse;
import com.demo_crud.demo.entity.User;
import com.demo_crud.demo.exception.AppException;
import com.demo_crud.demo.repository.RoleRepository;
import com.demo_crud.demo.repository.UserRepository;
import com.demo_crud.demo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;

@AutoConfigureMockMvc
@SpringBootTest(classes = DemoApplication.class)
@TestPropertySource("/test.properties")
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    private UserCreationRequest request;
    private UserResponse response;
    private LocalDate dob;
    private User user;
    private ObjectMapper objectMapper;
    private UserUpdateRequest updateRequest;

    @BeforeEach
    void initData() {
        dob = LocalDate.of(2004, 1, 1);
        request = UserCreationRequest.builder()
                .firstName("vu")
                .lastName("vu")
                .username("vunguyenduc3648")
                .password("vunguyenduc3648")
                .dob(dob)
                .build();

        updateRequest = UserUpdateRequest.builder()
                .firstName("ducvu")
                .lastName("ducvu")
                .username("kanekiken")
                .password("kanekiken")
                .dob(dob)
                .build();
        response = UserResponse.builder()
                .id("364823123")
                .firstName("ducvu")
                .lastName("ducvu")
                .username("kanekiken")
                .dob(dob)
                .build();


        response = UserResponse.builder()
                .id("364823123")
                .firstName("vu")
                .lastName("vu")
                .username("vunguyenduc3648")
                .dob(dob)
                .build();

        user = User.builder()
                .id("364823123")
                .firstName("vu")
                .lastName("vu")
                .username("vunguyenduc3648")
                .dob(dob)
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void createUser_validRequest_success() throws Exception {
        Mockito.when(userRepository.existsByUsername(ArgumentMatchers.anyString())).thenReturn(false);
        Mockito.when(userRepository.save(ArgumentMatchers.any())).thenReturn(user);
        Mockito.when(roleRepository.findById(PredefinedRole.USER_ROLE)).thenReturn(Optional.empty());

        var response = userService.createUser(request);

        Assertions.assertThat(response.getId()).isEqualTo("364823123");
        Assertions.assertThat(response.getFirstName()).isEqualTo("vu");
        Assertions.assertThat(response.getLastName()).isEqualTo("vu");
        Assertions.assertThat(response.getUsername()).isEqualTo("vunguyenduc3648");
        Assertions.assertThat(response.getDob()).isEqualTo(dob);
    }

    @Test
    void createUser_userInvalid_fail() throws Exception {
        Mockito.when(userRepository.existsByUsername(ArgumentMatchers.anyString())).thenReturn(true);

        var exception = assertThrows(AppException.class, () -> userService.createUser(request));
        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1001);
        Assertions.assertThat(exception.getErrorCode().getMessage()).isEqualTo("user has been existed");
    }
    @Test
    @WithMockUser(username = "vunguyenduc3648")//co the gia lap user, role cua user dang login dung trong auth,author
    void getMyInfo_valid_success() throws Exception {
        Mockito.when(userRepository.findByUsername(ArgumentMatchers.anyString())).thenReturn(Optional.of(user));
        var response = userService.getMyInfo();
        Assertions.assertThat(response.getData().getUsername()).isEqualTo("vunguyenduc3648");
        Assertions.assertThat(response.getData().getId()).isEqualTo("364823123");
    }
    @Test
    @WithMockUser(username = "vunguyenduc3648")
    void getMyInfo_invalid_fail() throws Exception {
        Mockito.when(userRepository.findByUsername(ArgumentMatchers.anyString())).thenReturn(Optional.empty());
        var exception = assertThrows(AppException.class, () -> userService.getMyInfo());
        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1003);
        Assertions.assertThat(exception.getErrorCode().getMessage()).isEqualTo("user does not exist");
    }
    @Test
    void updateUser_validRequest_success() throws Exception {
        Mockito.when(userRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(ArgumentMatchers.any())).thenReturn(user);
        var response = userService.updateUser(user.getId(), updateRequest);
        Assertions.assertThat(response.getId()).isEqualTo("364823123");
        Assertions.assertThat(response.getFirstName()).isEqualTo("ducvu");
        Assertions.assertThat(response.getLastName()).isEqualTo("ducvu");
        Assertions.assertThat(response.getUsername()).isEqualTo("kanekiken");
    }
    @Test
    void UpdateUser_invalidRequest_fail() throws Exception {
        Mockito.when(userRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.empty());
        var exception = assertThrows(AppException.class, () -> userService.updateUser(user.getId(), updateRequest));
        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1003);
        Assertions.assertThat(exception.getErrorCode().getMessage()).isEqualTo("user does not exist");
    }
    @Test
    void getUserById_valid_success() throws Exception {
        Mockito.when(userRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.of(user));
        var response = userService.getUserById(user.getId());
        Assertions.assertThat(response.getData().getId()).isEqualTo("364823123");
        Assertions.assertThat(response.getData().getUsername()).isEqualTo("vunguyenduc3648");
    }
    @Test
    void getUserById_invalid_fail() throws Exception {
        Mockito.when(userRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.empty());
        var exception = assertThrows(AppException.class, () -> userService.getUserById(user.getId()));
        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1003);
        Assertions.assertThat(exception.getErrorCode().getMessage()).isEqualTo("user does not exist");
    }
    @Test
    void deleteUser_valid_success() throws Exception {
        userService.deleteUser(UUID.randomUUID());
        Mockito.verify(userRepository).deleteById(String.valueOf(UUID.randomUUID()));
    }
    @Test
    void getAllUsers_valid_success() throws Exception {
        Mockito.when(userRepository.findAll()).thenReturn(List.of(user));
        var response = userService.getAllUsers();
        
    }
}
