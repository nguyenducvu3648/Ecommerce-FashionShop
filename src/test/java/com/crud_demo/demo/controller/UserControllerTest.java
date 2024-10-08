package com.crud_demo.demo.controller;

import com.demo_crud.demo.dto.request.UserCreationRequest;
import com.demo_crud.demo.dto.response.UserResponse;
import com.demo_crud.demo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.time.LocalDate;


@ContextConfiguration
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private UserCreationRequest request;
    private UserResponse response;
    private LocalDate dob;

    private ObjectMapper objectMapper;  // Đưa ObjectMapper vào class level

    @BeforeEach
    void initData() {
        // Khởi tạo dữ liệu test
        dob = LocalDate.of(2024, 1, 1);
        request = UserCreationRequest.builder()
                .firstName("vu")
                .lastName("vu")
                .username("vu")
                .password("vu")
                .dob(dob)
                .build();
        response = UserResponse.builder()
                .id("364823123")
                .firstName("vu")
                .lastName("vu")
                .username("vu")
                .dob(dob)
                .build();

        // Khởi tạo ObjectMapper chỉ 1 lần trong @BeforeEach
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void createUser_validRequest_success() throws Exception {
        // Chuyển đối tượng request thành chuỗi JSON với ObjectMapper
        String content = objectMapper.writeValueAsString(request);

        // Giả lập hành vi của UserService
        Mockito.when(userService.createUser(ArgumentMatchers.any())).thenReturn(response);

        // Thực hiện yêu cầu HTTP POST tới /users và kiểm tra phản hồi
        mockMvc.perform(MockMvcRequestBuilders.post("/identity/users/sign-up")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }
}
