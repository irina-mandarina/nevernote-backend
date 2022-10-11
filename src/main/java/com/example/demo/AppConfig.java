package com.example.demo;

import com.example.demo.Repositories.Store;
import com.example.demo.Services.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = UserServiceImpl.class)
public class AppConfig {
    @Bean
    public Store getStore() {
        return new Store();
    }
}