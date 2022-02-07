package com.inhabas.api.security.jwtUtils;

import com.inhabas.api.security.domain.AuthUser;
import com.inhabas.api.security.domain.AuthUserService;
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

    private final JwtTokenProvider jwtTokenProvider;

    private final AuthenticationFailureHandler failureHandler;

    private AuthenticationSuccessHandler successHandler; // this is not necessary, for future usage

    /**
     * JwtAuthenticationProcessingFilter is required these fields.
     * you can modify this filter's functionality by changing these fields.
     * @param authUserService Check whether request-user exist our database or not.
     * @param jwtTokenProvider Authenticate jwt token.
     * @param failureHandler In the case of the invalid jwt token,
     *                       default behavior is just to redirect to controller to response "Invalid_Token" error.
     */
    public JwtAuthenticationProcessingFilter(AuthUserService authUserService,
                                             JwtTokenProvider jwtTokenProvider,
                                             AuthenticationFailureHandler failureHandler) {
        this.authUserService = authUserService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.failureHandler = failureHandler;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = jwtTokenProvider.resolveToken(request);

        if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            try {
                JwtTokenDecodedInfo decodedInfo = jwtTokenProvider.authenticate(token);
                AuthUser authUser = authUserService.loadUser(decodedInfo.getAuthUserId());
                JwtAuthenticationToken authentication = new JwtAuthenticationToken(authUser, decodedInfo.getGrantedAuthorities());

                // Authentication success
                successfulAuthentication(request, response, filterChain, authentication);

                // keep going to process client's request
                filterChain.doFilter(request, response);

            } catch (InvalidJwtTokenException e) {
                // Authentication failed
                this.unsuccessfulAuthentication(request, response, e);
            }
        }
    }

    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                  Authentication authResult) throws IOException, ServletException {
        authResult.setAuthenticated(true);
        ((JwtAuthenticationToken) authResult).setDetails(request.getRemoteAddr());
        SecurityContextHolder.getContext().setAuthentication(authResult);
        this.logger.trace("jwt token authentication success!");
        this.logger.debug(
                LogMessage.format("jwt filtering!! : {}", ((AuthUser) authResult.getPrincipal()).getEmail()));

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
