package com.demo_crud.demo.Configuration;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary configKey() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dcwz2asxe");
        config.put("api_key", "836848756741891");
        config.put("api_secret", "BsOkamcFNfGFfVwiwAWTzdLPjqo");
        return new Cloudinary(config);
    }
}
