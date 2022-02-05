package com.inhabas.api.security.jwtUtils;

import com.inhabas.api.security.domain.AuthUser;
import com.inhabas.api.security.domain.AuthUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

    private final AuthUserService authUserService;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = jwtTokenProvider.resolveToken(request);

        if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            try {
                JwtTokenDecodedInfo decodedInfo = jwtTokenProvider.authenticate(token);
                AuthUser authUser = authUserService.loadUser(decodedInfo.getAuthUserId());
                JwtAuthenticationToken authentication = new JwtAuthenticationToken(authUser, decodedInfo.getGrantedAuthorities());
                authentication.setAuthenticated(true);
                authentication.setDetails(request.getRemoteAddr());

                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("jwt filtering!! : {}", ((AuthUser) authentication.getPrincipal()).getEmail());
            } catch (InvalidJwtTokenException e) {

                // access token 인증 실패 시..
                log.debug(e.getMessage());
            }

        }

        filterChain.doFilter(request, response);
    }

}
