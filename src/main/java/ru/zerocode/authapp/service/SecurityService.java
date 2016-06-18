package ru.zerocode.authapp.service;

/**
 * Интерфейс для секурити сервиса
 * */
public interface SecurityService {
    // Пользователь Админ
    Boolean isAdmin();
    // Пользователь Юзер
    Boolean isUser();
}

