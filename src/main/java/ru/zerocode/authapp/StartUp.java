package ru.zerocode.authapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Spring Boot Application Entry point
 * */
@SpringBootApplication
@ComponentScan("ru.zerocode.authapp")
public class StartUp {
    public static void main(String[] args) {
        SpringApplication.run(StartUp.class, args);
    }
}
