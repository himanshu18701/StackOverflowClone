# StackOverflow Clone

A simple StackOverflow clone built using Spring Boot and Thymeleaf. This project includes features for posting questions, answering them, and user authentication. It also integrates email notifications using SMTP and image uploading via Cloudinary.

## Features

- User authentication and registration
- Posting questions and answers
- Email notifications using SMTP
- Image uploading via Cloudinary

## Prerequisites

- Java 11 or higher
- Maven
- MySQL (or any other preferred database)

## Getting Started

### Clone the Repository

```sh
git clone https://github.com/yourusername/stackoverflow-clone.git
cd stackoverflow-clone
```

# Project Setup Guide

## Setup Database

1. Create a new database in MySQL (or your preferred database).
2. Update the `application.properties` file with your database configuration.

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/your_database_name
spring.datasource.username=your_database_username
spring.datasource.password=your_database_password
spring.jpa.hibernate.ddl-auto=update
```

## Configure SMTP for Email Notifications

Update the `application.properties` file with your SMTP configuration.

```properties
spring.mail.host=smtp.your-email-provider.com
spring.mail.port=587
spring.mail.username=your_email@example.com
spring.mail.password=your_email_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

# Configure Cloudinary for Image Uploading

To enable image uploading using Cloudinary, you need to configure your application with Cloudinary credentials. Follow the steps below to create and update the `CloudinaryConfig` class.

## Step 1: Create a CloudinaryConfig class

If the `CloudinaryConfig` class is not already present in your project, create it under the `com.example.config` package.

```java
package com.example.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "your_cloudinary_cloud_name",
            "api_key", "your_cloudinary_api_key",
            "api_secret", "your_cloudinary_api_secret"
        ));
    }
}
```
