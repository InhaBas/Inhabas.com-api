//package com.inhabas.api.auth.domain.token;
//
//import static org.junit.jupiter.api.Assertions.assertNull;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//
//import com.inhabas.api.auth.domain.token.jwtUtils.JwtTokenProvider;
//import com.inhabas.api.auth.domain.token.jwtUtils.JwtTokenResolver;
//import com.inhabas.api.auth.domain.token.jwtUtils.refreshToken.RefreshTokenRepository;
//import com.inhabas.api.auth.domain.token.securityFilter.InvalidJwtTokenHandler;
//import com.inhabas.api.auth.domain.token.securityFilter.TokenAuthenticationProcessingFilter;
//import com.inhabas.api.auth.domain.token.securityFilter.UserPrincipalService;
//import java.io.IOException;
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Spy;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.context.annotation.Import;
//import org.springframework.mock.web.MockHttpServletRequest;
//import org.springframework.mock.web.MockHttpServletResponse;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
//import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
//
//@ExtendWith(MockitoExtension.class)
//@Import({RefreshTokenRepository.class})
//public class TokenAuthenticationProcessingFilterTest {
//
//    @Mock
//    private RefreshTokenRepository refreshTokenRepository;
//
//    @Mock
//    private UserPrincipalService userPrincipalService;
//
//    @Mock
//    private InvalidJwtTokenHandler invalidJwtTokenHandler;
//
//    @Spy
//    private JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(refreshTokenRepository);
//
//    @Spy
//    private JwtTokenResolver jwtTokenResolver = new JwtTokenResolver();
//
//    @InjectMocks
//    private TokenAuthenticationProcessingFilter tokenAuthenticationProcessingFilter;
//
//    @Mock
//    private FilterChain filterChain;
//
//    @BeforeEach
//    public void setUp() {
//        SecurityContextHolder.getContext().setAuthentication(null);
//    }
//
//    @DisplayName("jwt 토큰으로 인증을 성공한다.")
//    @Test
//    public void successfulAuthenticationTest() throws ServletException, IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
//        //given
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        MockHttpServletResponse response = new MockHttpServletResponse();
//        doNothing().when(filterChain).doFilter(any(), any());
//
//        String accessToken = jwtTokenProvider.createAccessToken(getAuthentication());
//        request.addHeader("Authorization", "Bearer " + accessToken);
//
//        given(userPrincipalService.loadUserPrincipal(any())).willReturn(12171652);
//
//        //when
//        Method doFilterInternal = tokenAuthenticationProcessingFilter.getClass().getDeclaredMethod("doFilterInternal", HttpServletRequest.class, HttpServletResponse.class, FilterChain.class);
//        doFilterInternal.setAccessible(true);
//        doFilterInternal.invoke(tokenAuthenticationProcessingFilter, request, response, filterChain);
//
//        //then
//        verify(filterChain, times(1)).doFilter(any(), any());
//        assertTrue(SecurityContextHolder.getContext().getAuthentication().isAuthenticated());
//    }
//
//    @DisplayName("유효하지 않은 jwt 토큰으로 인증을 시도하면, 400 request")
//    @Test
//    public void invalidJwtTokenResponse() throws ServletException, IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
//        // custom exception code 가 필요할 수도
//        //given
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        MockHttpServletResponse response = new MockHttpServletResponse();
//
//        String accessToken = "invalid access token";
//        request.addHeader("Authorization", "Bearer " + accessToken);
//
//        //when
//        Method doFilterInternal = tokenAuthenticationProcessingFilter.getClass().getDeclaredMethod("doFilterInternal", HttpServletRequest.class, HttpServletResponse.class, FilterChain.class);
//        doFilterInternal.setAccessible(true);
//        doFilterInternal.invoke(tokenAuthenticationProcessingFilter, request, response, filterChain);
//
//        //then
//        assertNull(SecurityContextHolder.getContext().getAuthentication());
//        verify(invalidJwtTokenHandler, times(1)).onAuthenticationFailure(any(), any(), any());
//        verify(filterChain, times(0)).doFilter(any(), any());
//    }
//
//    @DisplayName("토큰 없이 요청할 경우 아무것도 하지 않고 다음 필터 호출한다.")
//    @Test
//    public void passRequestToNextFilter() throws ServletException, IOException {
//        //given
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        MockHttpServletResponse response = new MockHttpServletResponse();
//        doNothing().when(filterChain).doFilter(any(), any());
//
//        //when
//        tokenAuthenticationProcessingFilter.doFilter(request, response, filterChain);
//
//        //then
//        verify(filterChain, times(1)).doFilter(any(), any());
//    }
//
//    private OAuth2AuthenticationToken getAuthentication() {
//        List<SimpleGrantedAuthority> authorities =
//                List.of(new SimpleGrantedAuthority("ROLE_ANONYMOUS"), new SimpleGrantedAuthority("TEAM_IT"));
//        Map<String, Object> attributes = new HashMap<>() {{
//            put("sub", "1249846925629348");
//            put("name", "유동현");
//            put("picture", "blahblah");
//            put("email", "my@gmail.com");
//            put("locale", "ko");
//        }};
//        return new OAuth2AuthenticationToken(
//                new DefaultOAuth2User(authorities, attributes, "sub"), authorities, "google");
//    }
//
//
//}
