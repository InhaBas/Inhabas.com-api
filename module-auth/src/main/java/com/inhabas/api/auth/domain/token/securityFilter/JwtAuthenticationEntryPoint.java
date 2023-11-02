package com.inhabas.api.auth.domain.token.securityFilter;

import com.inhabas.api.auth.domain.token.exception.MissingTokenException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        if (authException instanceof InsufficientAuthenticationException && authException.getCause() instanceof MissingTokenException) {
            response.sendRedirect("/login");
            return;
        }

        response.sendError(HttpServletResponse.SC_FORBIDDEN);
    }
}
