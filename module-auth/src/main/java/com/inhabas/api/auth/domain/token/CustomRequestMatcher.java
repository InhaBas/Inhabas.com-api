package com.inhabas.api.auth.domain.token;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 기존 matches와 달리 request와 일치하지 않으면 True 반환
 */
public class CustomRequestMatcher implements RequestMatcher {

    private final OrRequestMatcher matcher;

    public CustomRequestMatcher(List<String> skipPaths) {
        final List<RequestMatcher> requestMatchers = skipPaths.stream()
                .map(AntPathRequestMatcher::new)
                .collect(Collectors.toList());
        this.matcher = new OrRequestMatcher(requestMatchers);
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        return !matcher.matches(request);
    }
}
