package com.example.advancedtaskmanagement.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnvConfig {

    private final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

    @Bean
    public String dbUrl() {
        return dotenv.get("POSTGRES_DB");
    }

    @Bean
    public String dbUsername() {
        return dotenv.get("POSTGRES_USER");
    }

    @Bean
    public String dbPassword() {
        return dotenv.get("POSTGRES_PASSWORD");
    }

    @Bean
    public String adminEmail() {
        return dotenv.get("SPRING_SECURITY_USER");
    }

    @Bean
    public String adminPassword() {
        return dotenv.get("SPRING_SECURITY_PASSWORD");
    }

    @Bean
    public String jwtSecret() {
        return dotenv.get("JWT_SECRET");
    }

}
