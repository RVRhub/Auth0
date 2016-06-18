package ru.zerocode.authapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Контроллер индекс страницы
 * */
@Controller
public class HomeController {
    @RequestMapping("/")
    public String homeEndpoint(){
        return "forward:/view/index.html";
    }
}
