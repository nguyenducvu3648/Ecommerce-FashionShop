package com.demo_crud.demo.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final String[] PUBLIC_ENDPOINTS = {
            "/users/sign-up",
            "/auth/log-in",
            "/auth/introspect",
            "/auth/log-out",
            "/auth/refreshToken",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/webjars/**",
    };

    @Autowired
    private CustomJwtDecoder customJwtDecoder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.authorizeHttpRequests(request ->
                request.requestMatchers(HttpMethod.GET ,PUBLIC_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS).permitAll()
                        .anyRequest().authenticated());
        httpSecurity.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(customJwtDecoder)
                        .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
        );

        return httpSecurity.build();
    }
    //chuyen doi jwt thanh thong tin xac thuc
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix(""); // Không thêm prefix ROLE_
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles"); // Claim chứa quyền
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return converter;
    }

}
