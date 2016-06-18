package ru.zerocode.authapp.security;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.internal.org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.zerocode.authapp.model.Identity;

import java.util.ArrayList;
import java.util.Map;

/**
 * Утилиты работы с токеном
 * */
@Component
public class TokenUtils {
    // Поле ClientID в application.yml
    @Value("${auth.token.id}")
    private String CLIENT_ID;

    // Поле Secret в application.yml
    @Value("${auth.token.secret}")
    private String CLIENT_SECRET;

    /**
     * Получение и валидация Сущности пользователя по токену
     * */
    Identity getIdentity(String token){
        try {
            // Создаем билдер
            Identity.Builder identityBuilder = new Identity.Builder();
            // Если в настройках ничего нет
            if(CLIENT_SECRET == null || CLIENT_ID == null)
                throw new Exception("No Secret/ID is Set");

            // Декодим строку секрета
            byte[] secret = Base64.decodeBase64(CLIENT_SECRET);
            // Верифиаем с клиентИд + Секрет токен
            Map<String,Object> decodedPayload = new JWTVerifier(secret, CLIENT_ID).verify(token);
            // Билдим объект
            identityBuilder
                    .name((String)decodedPayload.get("name"))
                    .id((String)decodedPayload.get("sub"))
                    .email((String)decodedPayload.get("email"))
                    .expires((Integer)(decodedPayload.get("exp")))
                    .picture((String)decodedPayload.get("picture"))
                    .nickName((String)decodedPayload.get("nickname"));
            // Билдим роли, из массива в Коллекцию Авторитиес
            ArrayList<String> rolesList = (ArrayList)decodedPayload.get("roles");
            String authorities = String.join(",", rolesList);
            identityBuilder.authorities(authorities);
            // Билд
            return identityBuilder.build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Всё очень плохо
        return null;
    }
}
