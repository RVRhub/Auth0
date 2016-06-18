package ru.zerocode.authapp.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import ru.zerocode.authapp.model.protocol.RestError;
import ru.zerocode.authapp.model.protocol.RestResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class EntryPointUnauthorizedHandler implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse,
                         AuthenticationException e) throws IOException, ServletException {
        RestResponse res = new RestResponse(false);
        res.addError(new RestError(401,"Access Denied"));
        httpServletResponse.getWriter().append(objectMapper.writeValueAsString(res));
        httpServletResponse.setHeader("Content-Type","application/json");
        httpServletResponse.setStatus(401);
    }
}
