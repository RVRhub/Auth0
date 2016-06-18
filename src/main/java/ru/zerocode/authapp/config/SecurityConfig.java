/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 */

package ru.zerocode.authapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import ru.zerocode.authapp.security.AuthenticationTokenFilter;
import ru.zerocode.authapp.security.EntryPointUnauthorizedHandler;
import ru.zerocode.authapp.service.SecurityService;

/**
 * Конфигурация Spring Security
 * */
@SuppressWarnings("SpringJavaAutowiringInspection")
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private EntryPointUnauthorizedHandler unauthorizedHandler;
    @Autowired
    private AccessDeniedHandler accessDeniedHandler;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private SecurityService securityService;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .headers()
                    .frameOptions().disable()
                    .and()
                .httpBasic().disable() // Отключаем базовую авторизацию
                .exceptionHandling()
                    .authenticationEntryPoint(unauthorizedHandler)  // Точка входа авториазции
                    .accessDeniedHandler(accessDeniedHandler)   // Хендлер 401 доступа
                    .and()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Отключаем сессии
                    .and()
                .authorizeRequests() // Для всех запросов которые не в этом списке, требуется авторизация
                    .antMatchers(
                            "/","/view/**","/app/**","/lib/**","/boot.png",
                            "/api/public","/api/admin","/api/user"
                    ).permitAll()
                .anyRequest().authenticated();
        httpSecurity.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    /**
     * Метод (заглушка) для отключения Basic авторизации в Boot
     * */
    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    /**
     * Фильтр запросов авторизации
     * */
    @Bean
    public AuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
        AuthenticationTokenFilter authenticationTokenFilter = new AuthenticationTokenFilter();
        authenticationTokenFilter.setAuthenticationManager(authenticationManagerBean());
        return authenticationTokenFilter;
    }


    /**
     * Бин сервиса для проверки авторизации в контроллере PreAuthorize
     * */
    @Bean
    public SecurityService securityService() {
        return this.securityService;
    }

    /**
     * Форвард на индекс
     * */
    @Bean
    public WebMvcConfigurerAdapter forwardToIndex() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                registry.addViewController("/").setViewName("forward:/view/index.html");
            }
        };
    }
}
