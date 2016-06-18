package ru.zerocode.authapp.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import ru.zerocode.authapp.model.protocol.RestError;
import ru.zerocode.authapp.model.protocol.RestResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Хендлер доступ запрещен
 * */
@Component
public class AuthAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        RestResponse res = new RestResponse(false);
        res.addError(new RestError(401,"Access Denied"));
        response.getWriter().append(objectMapper.writeValueAsString(res));
        response.setHeader("Content-Type","application/json");
        response.setStatus(401);
    }
}
