package com.inhabas.api.auth.domain.token.securityFilter;

import com.inhabas.api.auth.domain.token.TokenResolver;
import com.inhabas.api.auth.domain.token.exception.InvalidTokenException;
import com.inhabas.api.auth.domain.token.exception.MissingTokenException;
import com.inhabas.api.auth.domain.token.jwtUtils.JwtAuthenticationToken;
import com.inhabas.api.auth.domain.token.jwtUtils.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final TokenResolver tokenResolver;

    public JwtAuthenticationFilter(
            RequestMatcher requestMatcher,
            JwtTokenUtil jwtTokenUtil,
            TokenResolver tokenResolver) {

        super(requestMatcher); // only work for requests with this pattern
        this.jwtTokenUtil = jwtTokenUtil;
        this.tokenResolver = tokenResolver;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        final String token = tokenResolver.resolveAccessTokenOrNull(request);
        if (!jwtTokenUtil.validate(token))
            throw new InvalidTokenException();
        final JwtAuthenticationToken authRequest = JwtAuthenticationToken.of(token);

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        log.debug("jwt token authentication success!");

        chain.doFilter(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        log.info("Failed to process authentication request", failed);
        if (failed instanceof MissingTokenException) {
            response.sendRedirect("/login");
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
