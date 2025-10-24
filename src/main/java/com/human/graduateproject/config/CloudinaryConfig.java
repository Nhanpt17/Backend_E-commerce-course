package com.human.graduateproject.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary() {

        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dx9yg1sze",
                "api_key", "791762631123281",
                "api_secret", "WJLoiQaPWujX_vuk4cKjy6DS0qw",
                "secure", true
        ));
    }
}
