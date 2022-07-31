package com.inhabas.api.auth.domain.token;

import com.inhabas.api.auth.domain.token.jwtUtils.JwtTokenResolver;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

public class JwtTokenResolverTest {

    private final JwtTokenResolver jwtTokenResolver = new JwtTokenResolver();

    @DisplayName("http request 쿠키로부터 토큰을 꺼낸다.")
    @Test
    public void resolveTokenFromHttpRequestTest() {

        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer header.body.signature");

        //when
        String resolvedToken = jwtTokenResolver.resolveTokenOrNull(request);

        //then
        Assertions.assertThat(resolvedToken).isEqualTo("header.body.signature");
    }

    @DisplayName("http request 에 쿠키가 설정되어 있지 않아서 null을 반환한다.")
    @Test
    public void cannotResolveTokenFromHttpRequestTest() {

        //given
        MockHttpServletRequest request = new MockHttpServletRequest();

        //when
        String resolvedToken = jwtTokenResolver.resolveTokenOrNull(request);

        //then
        Assertions.assertThat(resolvedToken).isNull();
    }

    @DisplayName("Bearer 토큰이 아니면 null 을 반환한다.")
    @Test
    public void cannotResolveInvalidTokenFromHttpRequestTest() {

        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "No-Bearer header.body.signature");

        //when
        String resolvedToken = jwtTokenResolver.resolveTokenOrNull(request);

        //then
        Assertions.assertThat(resolvedToken).isNull();
    }
}
