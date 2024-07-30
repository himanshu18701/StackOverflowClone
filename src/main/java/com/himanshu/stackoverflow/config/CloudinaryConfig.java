package com.himanshu.stackoverflow.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dny8hbnha",
                "api_key", "943334443551986",
                "api_secret", "zIteA9rcA0ZffAmM-7QiBhcjEw0"
        ));
    }
}