package com.example.springtest.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "chat.encryption")
public class EncryptionConfig {
    
    /**
     * Secret key for AES-256 encryption
     * IMPORTANT: Change this in production!
     */
    private String secretKey = "MySecretKey12345678901234567890SecureKey!@#";
}
