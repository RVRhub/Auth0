package ru.zerocode.authapp.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import ru.zerocode.authapp.model.Identity;
import ru.zerocode.authapp.model.protocol.RestError;
import ru.zerocode.authapp.model.protocol.RestResponse;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Фильтр точки входа, авторизации для каждого запроса
 * */
public class AuthenticationTokenFilter extends UsernamePasswordAuthenticationFilter {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private TokenUtils tokenUtils;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httResponse = (HttpServletResponse) response;
        try {
            // Смотрим заголовок запроса
            String header = httpRequest.getHeader("Authorization");
            // Если установлен хедер авторизации
            if (header != null)
                // Если это OAuth2 тип (Bearer TOKEN)
                if(header.startsWith("Bearer ")) {
                    // Берем токен
                    String authToken = header.substring(7);
                    // Парсим и верифаим токен
                    Identity identity = tokenUtils.getIdentity(authToken);
                    // Если не прошел верификацию
                    if(identity == null)
                        throw new Exception("Invalid or Expired Token");
                    // Залогиниваем пользователя через Секурити Контекст
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(identity, null, identity.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            // Следующие фильтры
            chain.doFilter(request, response);
        }catch (Exception ex){
            // Если всё очень плохо, пишем прямо в тело JSON
            RestResponse res = new RestResponse(false);
            res.addError(new RestError(401,"Access Denied ["+ex.getMessage()+"]"));
            httResponse.setContentType("application/json");
            httResponse.getWriter().append(objectMapper.writeValueAsString(res));
            httResponse.setStatus(401);
        }
    }
}
