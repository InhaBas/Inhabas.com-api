package com.inhabas.api.auth.domain.token.securityFilter;

import com.inhabas.api.auth.domain.token.TokenProvider;
import com.inhabas.api.auth.domain.token.jwtUtils.InvalidJwtTokenException;
import com.inhabas.api.auth.domain.token.jwtUtils.JwtAuthenticationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenAuthenticationProcessingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(TokenAuthenticationProcessingFilter.class);

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private final UserPrincipalService userPrincipalService;

    private final AuthenticationFailureHandler failureHandler;

    private final TokenProvider tokenProvider;

    private AuthenticationSuccessHandler successHandler; // this is not necessary, for future usage

    /**
     * JwtAuthenticationProcessingFilter is required these fields.
     * you can modify this filter's functionality by changing these fields.
     * @param failureHandler In the case of the invalid jwt token,
     *                       default behavior is just to redirect to controller to response "Invalid_Token" error.
     */
    public TokenAuthenticationProcessingFilter(TokenProvider tokenProvider,
                                               AuthenticationFailureHandler failureHandler,
                                               UserPrincipalService userPrincipalService) {
        this.failureHandler = failureHandler;
        this.tokenProvider = tokenProvider;
        this.userPrincipalService = userPrincipalService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = this.resolveTokenOrNull(request);

        if (SecurityContextHolder.getContext().getAuthentication() == null && StringUtils.hasText(token)) {

            try {
                if (!tokenProvider.validate(token))
                    throw new InvalidJwtTokenException();

                JwtAuthenticationResult authentication = (JwtAuthenticationResult) tokenProvider.decode(token);
                Object principal = userPrincipalService.loadUserPrincipal(authentication);
                authentication.setPrincipal(principal);

                // handle for authentication success
                successfulAuthentication(request, response, filterChain, authentication);

            } catch (InvalidJwtTokenException | UserPrincipalNotFoundException e) {
                // Authentication failed redirection
                this.unsuccessfulAuthentication(request, response, e);
                return;
            }
        }

        // If client doesn't have any token or under redirection, keep going to process client's request
        filterChain.doFilter(request, response);
    }

    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                  Authentication authResult) throws IOException, ServletException {

        authResult.setAuthenticated(true);
        ((JwtAuthenticationResult) authResult).setDetails(request.getRemoteAddr());
        SecurityContextHolder.getContext().setAuthentication(authResult);
        logger.trace("jwt token authentication success!");

        if (this.successHandler != null) {
            logger.trace("Handling authentication success");
            this.successHandler.onAuthenticationSuccess(request, response, authResult);
        }
    }


    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {

        SecurityContextHolder.clearContext();
        logger.trace("jwt token validation fail", failed);
        logger.trace("Cleared SecurityContextHolder");
        logger.trace("Handling authentication failure");
        this.failureHandler.onAuthenticationFailure(request, response, failed); // 리다이렉트 해야함?
    }


    private String resolveTokenOrNull(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer "))
            return bearerToken.substring(7);
        else
            return null;
    }
}
