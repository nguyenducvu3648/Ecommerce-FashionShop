package com.crud_demo.demo.service;

import com.demo_crud.demo.DemoApplication;
import com.demo_crud.demo.dto.request.UserCreationRequest;
import com.demo_crud.demo.dto.response.UserResponse;
import com.demo_crud.demo.entity.User;
import com.demo_crud.demo.exception.AppException;
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
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

@AutoConfigureMockMvc
@SpringBootTest(classes = DemoApplication.class)
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    private UserCreationRequest request;
    private UserResponse response;
    private LocalDate dob;
    private User user;
    private ObjectMapper objectMapper;

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

        var response = userService.createUser(request);
        Assertions.assertThat(response.getId()).isEqualTo("364823123");
        Assertions.assertThat(response.getFirstName()).isEqualTo("vu");
        Assertions.assertThat(response.getLastName()).isEqualTo("vu");
        Assertions.assertThat(response.getUsername()).isEqualTo("vunguyenduc3648");
        Assertions.assertThat(response.getDob()).isEqualTo(dob);
    }
    @Test
    void createUser_userInvalid() throws Exception {
        Mockito.when(userRepository.existsByUsername(ArgumentMatchers.anyString())).thenReturn(true);

        var exception = assertThrows(AppException.class, () -> userService.createUser(request));
        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1001);
        Assertions.assertThat(exception.getErrorCode().getMessage()).isEqualTo("user has been existed");
    }
}
