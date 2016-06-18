package ru.zerocode.authapp.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.zerocode.authapp.annotations.CurrentIdentity;
import ru.zerocode.authapp.model.Identity;
import ru.zerocode.authapp.model.SampleObject;

/**
 * Рест контроллер для АПИ
 * */
@RestController
@RequestMapping(value="/api",produces = "application/json")
public class ApiController {

    /**
     * Доступен для всех, определен в SecurityConfig как общедоступный
     * Не требует ПреАвторизации
     * */
    @RequestMapping("/public")
    public SampleObject publicEndpoint(){
        return new SampleObject("PUBLIC","This is public Endpoint, Anyone can access it.",null);
    }

    /**
     * Доступный только для пользвоателей прошедшим SecurityService.isAdmin т.е. только для роли Админ
     * */
    @PreAuthorize("@securityService.isAdmin()")
    @RequestMapping("/admin")
    public SampleObject adminEndpoint(@CurrentIdentity Identity identity){
        return new SampleObject(
                "ADMIN",
                "Hello, "+identity.getNickName()+" This is ADMIN Only Endpoint, Only ADMIN can Access it.",
                identity);
    }

    /**
     * Доступный только для пользвоателей прошедшим SecurityService.isUser т.е. только для роли Юзер
     * */
    @PreAuthorize("@securityService.isUser()")
    @RequestMapping("/user")
    public SampleObject userEndpoint(@CurrentIdentity Identity identity){
        return new SampleObject(
                "USER",
                "Hello, "+identity.getNickName()+" This is USER Endpoint, Only users with USER Role can access it.",
                identity);
    }
}
