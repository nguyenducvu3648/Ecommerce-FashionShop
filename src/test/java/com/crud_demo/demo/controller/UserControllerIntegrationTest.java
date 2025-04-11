package com.crud_demo.demo.controller;

import com.demo_crud.demo.DemoApplication;
import com.demo_crud.demo.dto.request.UserCreationRequest;
import com.demo_crud.demo.dto.response.UserResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.time.LocalDate;

@Slf4j
@SpringBootTest(classes = DemoApplication.class)
@AutoConfigureMockMvc
@Testcontainers
public class UserControllerIntegrationTest {

    @Container
    static final MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:latest");
    @DynamicPropertySource
    static void configureDataSource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
        registry.add("spring.datasource.driver-class-name",()->"com.mysql.cj.jdbc.Driver");
        registry.add("spring.jpa.hibernate.ddl-auto",()->"update");
    }

    @Autowired
    private MockMvc mockMvc;
    private UserCreationRequest request;
    private UserResponse response;
    private LocalDate dob;

    private ObjectMapper objectMapper;  // Đưa ObjectMapper vào class level

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

        // Khởi tạo ObjectMapper chỉ 1 lần trong @BeforeEach
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void createUser_validRequest_success() throws Exception {

        String content = objectMapper.writeValueAsString(request);


        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users/sign-up")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("1000"))
                .andExpect(MockMvcResultMatchers.jsonPath("data.username").value("vunguyenduc3648"));
    }
    @Test
    void createUser_usernameInvalid_fail() throws Exception {

        request.setUsername("vu");
        String content = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users/sign-up")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("1002"))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("username must be at least 8 character"));
    }
}
