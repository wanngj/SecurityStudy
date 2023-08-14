package com.example.springsecurity02;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.springframework.security.core.context.SecurityContextHolder.MODE_INHERITABLETHREADLOCAL;

@SpringBootApplication
public class SpringSecurity02Application {

    public static void main(String[] args) {
        System.setProperty("spring.security.strategy",MODE_INHERITABLETHREADLOCAL);
        SpringApplication.run(SpringSecurity02Application.class, args);
    }

}
