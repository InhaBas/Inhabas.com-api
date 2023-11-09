package com.inhabas.api.auth.domain.token.securityFilter;

import com.inhabas.api.auth.domain.token.TokenAuthenticationResult;
import com.inhabas.api.auth.domain.token.TokenUtil;
import com.inhabas.api.auth.domain.token.TokenResolver;
import com.inhabas.api.auth.domain.token.exception.InvalidTokenException;
import com.inhabas.api.auth.domain.token.jwtUtils.JwtAuthenticationResult;
import com.inhabas.api.auth.domain.token.jwtUtils.JwtAuthenticationToken;
import com.inhabas.api.auth.domain.token.jwtUtils.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

        final String token = tokenResolver.resolveTokenOrNull(request);
        if (!jwtTokenUtil.validate(token))
            throw new InvalidTokenException();
        final JwtAuthenticationToken authRequest = JwtAuthenticationToken.of(token);

//        final Object principal = userPrincipalService.loadUserPrincipal(authentication);
//        authentication.setPrincipal(principal);

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
//        super.successfulAuthentication(request, response, chain, authResult);
        SecurityContextHolder.getContext().setAuthentication(authResult);
        System.out.println(SecurityContextHolder.getContext().getAuthentication());
        log.debug("jwt token authentication success!");

        chain.doFilter(request, response);
    }
}
