package me.dio.innovation.one.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {

    @Value("${spring.application.security.config.jwt.secret}")
    private String secret;

    @Value("${spring.application.security.config.jwt.prefix}")
    public String prefix;

    @Value("${spring.application.security.config.jwt.key}")
    public String key;

    @Value("${spring.application.security.config.jwt.expiration}")
    public Long expiration;

    public String getSecret() {
        return secret;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getKey() {
        return key;
    }

    public Long getExpiration() {
        return expiration;
    }
}
