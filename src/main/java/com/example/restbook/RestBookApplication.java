package com.example.restbook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class RestBookApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestBookApplication.class, args);
    }
}
