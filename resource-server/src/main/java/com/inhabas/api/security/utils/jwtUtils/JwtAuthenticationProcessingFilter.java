package com.inhabas.api.security.utils.jwtUtils;

import com.inhabas.api.security.domain.authUser.AuthUser;
import com.inhabas.api.security.domain.authUser.AuthUserDetail;
import com.inhabas.api.security.domain.authUser.AuthUserService;
import com.inhabas.api.security.domain.token.TokenProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.log.LogMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

    private final Log logger = LogFactory.getLog(getClass());

    private final AuthUserService authUserService;

    private final AuthenticationFailureHandler failureHandler;

    private final TokenProvider tokenProvider;

    private AuthenticationSuccessHandler successHandler; // this is not necessary, for future usage

    /**
     * JwtAuthenticationProcessingFilter is required these fields.
     * you can modify this filter's functionality by changing these fields.
     * @param authUserService Check whether request-user exist our database or not.
     * @param failureHandler In the case of the invalid jwt token,
     *                       default behavior is just to redirect to controller to response "Invalid_Token" error.
     */
    public JwtAuthenticationProcessingFilter(AuthUserService authUserService,
                                             TokenProvider tokenProvider,
                                             AuthenticationFailureHandler failureHandler) {
        this.authUserService = authUserService;
        this.failureHandler = failureHandler;
        this.tokenProvider = tokenProvider;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = tokenProvider.resolveToken(request);

        if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            try {
                JwtTokenDecodedInfo decodedInfo = (JwtTokenDecodedInfo) tokenProvider.authenticate(token);
                AuthUser authUser = authUserService.loadUser(decodedInfo.getAuthUserId());
                JwtAuthenticationToken authentication =
                        new JwtAuthenticationToken(AuthUserDetail.convert(authUser), decodedInfo.getGrantedAuthorities());

                // Authentication success redirection
                successfulAuthentication(request, response, filterChain, authentication);

            } catch (InvalidJwtTokenException e) {
                // Authentication failed redirection
                this.unsuccessfulAuthentication(request, response, e);
                return;
            }
        }

        // If client doesn't have any token, keep going to process client's request
        filterChain.doFilter(request, response);
    }

    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                  Authentication authResult) throws IOException, ServletException {
        authResult.setAuthenticated(true);
        ((JwtAuthenticationToken) authResult).setDetails(request.getRemoteAddr());
        SecurityContextHolder.getContext().setAuthentication(authResult);
        this.logger.trace("jwt token authentication success!");
        this.logger.debug(
                LogMessage.format("jwt filtering!! : {}", ((AuthUserDetail) authResult.getPrincipal()).getEmail()));

        if (this.successHandler != null) {
            this.logger.trace("Handling authentication failure success");
            this.successHandler.onAuthenticationSuccess(request, response, authResult);
        }
    }



    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              InvalidJwtTokenException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        this.logger.trace("jwt token validation fail", failed);
        this.logger.trace("Cleared SecurityContextHolder");
        this.logger.trace("Handling authentication failure");
        this.failureHandler.onAuthenticationFailure(request, response, failed);
    }

}
