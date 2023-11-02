package com.inhabas.api.auth.domain.token.securityFilter;

import com.inhabas.api.auth.domain.token.TokenProvider;
import com.inhabas.api.auth.domain.token.TokenResolver;
import com.inhabas.api.auth.domain.token.exception.InvalidTokenException;
import com.inhabas.api.auth.domain.token.jwtUtils.JwtAuthenticationResult;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final UserPrincipalService userPrincipalService;
    private final TokenProvider tokenProvider;
    private final TokenResolver tokenResolver;

    public JwtAuthenticationFilter(
            RequestMatcher requestMatcher,
            TokenProvider tokenProvider,
            TokenResolver tokenResolver,
            UserPrincipalService userPrincipalService) {

        super(requestMatcher); // only work for requests with this pattern
        this.tokenProvider = tokenProvider;
        this.tokenResolver = tokenResolver;
        this.userPrincipalService = userPrincipalService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        final String token = tokenResolver.resolveTokenOrNull(request);

        if (!tokenProvider.validate(token))
            throw new InvalidTokenException();

        final JwtAuthenticationResult authentication = (JwtAuthenticationResult) tokenProvider.decode(token);
        final Object principal = userPrincipalService.loadUserPrincipal(authentication);
        authentication.setPrincipal(principal);

        return authentication;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
//        super.successfulAuthentication(request, response, chain, authResult);
        SecurityContextHolder.getContext().setAuthentication(authResult);
        logger.debug("jwt token authentication success!");

        chain.doFilter(request, response);
    }
}
