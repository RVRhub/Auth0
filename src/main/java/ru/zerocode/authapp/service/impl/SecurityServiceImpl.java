package ru.zerocode.authapp.service.impl;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.zerocode.authapp.service.SecurityService;

/***
 * Имплементация интерфейса секурити сервиса
 *
 */
@Service
public class SecurityServiceImpl implements SecurityService {
    @Override
    public Boolean isAdmin() {
        // Смотрим роль Админ
        return (SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .contains(new SimpleGrantedAuthority("ADMIN")));
    }

    @Override
    public Boolean isUser() {
        // Смотрим роль Админ, так же можно добавить пользователя с ролью Админ считаться тоже Пользователем
        return (SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .contains(new SimpleGrantedAuthority("USER")));
    }
}
