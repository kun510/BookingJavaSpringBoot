package com.example.doancuoiky.hostel.configuration;

import com.cloudinary.Cloudinary;
import org.springframework.core.env.Environment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary(Environment env) {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", env.getProperty("cloudinary.cloud_name"));
        config.put("api_key", env.getProperty("cloudinary.api_key"));
        config.put("api_secret", env.getProperty("cloudinary.api_secret"));
        return new Cloudinary(config);
    }
}
