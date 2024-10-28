package com.demo_crud.demo.Configuration;

import com.demo_crud.demo.Constant.PredefinedRole;
import com.demo_crud.demo.entity.Role;
import com.demo_crud.demo.entity.User;
import com.demo_crud.demo.exception.AppException;
import com.demo_crud.demo.exception.ErrorCode;
import com.demo_crud.demo.repository.RoleRepository;
import com.demo_crud.demo.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@Configuration
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @NonFinal
    static final String ADMIN_USER_NAME = "admin";
    @NonFinal
    static final String ADMIN_PASSWORD = "admin";

    @Bean
    @ConditionalOnProperty(
            prefix = "spring.datasource",
            value = "driver-class-name",
            havingValue = "com.mysql.cj.jdbc.Driver")//Bean sẽ chỉ được tạo ra nếu giá trị của thuộc tính
        // spring.datasource.driver-class-name là com.mysql.cj.jdbc.Driver
    ApplicationRunner applicationRunner(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        log.info("Initializing application.....");
        return args -> {
            if (userRepository.findByUsername(ADMIN_USER_NAME).isEmpty()) {
                Optional<Role> adminRoleOpt = roleRepository.findById(PredefinedRole.ADMIN_ROLE);

                if (adminRoleOpt.isEmpty()) {
                    throw new AppException(ErrorCode.ADMIN_NOT_EXISTED);
                }

                // Chỉ gán role nếu role đã tồn tại
                Set<Role> roles = new HashSet<>();
                adminRoleOpt.ifPresent(roles::add);

                // Tạo tài khoản admin
                User adminUser = User.builder()
                        .username(ADMIN_USER_NAME)
                        .password(passwordEncoder.encode(ADMIN_PASSWORD))
                        .roles(roles)
                        .build();

                userRepository.save(adminUser);
                log.warn("Admin user has been created with default password: admin, please change it");
            }
            log.info("Application initialization completed .....");
        };
    }
}
