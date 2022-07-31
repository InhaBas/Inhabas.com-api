package com.inhabas.api.auth.domain.token;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.inhabas.api.auth.domain.token.jwtUtils.JwtTokenProvider;
import com.inhabas.api.auth.domain.token.jwtUtils.JwtTokenReIssuer;
import com.inhabas.api.auth.domain.token.jwtUtils.refreshToken.RefreshTokenNotFoundException;
import com.inhabas.api.auth.domain.token.jwtUtils.refreshToken.RefreshTokenRepository;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class JwtTokenReIssueTest {

    @InjectMocks
    private JwtTokenReIssuer tokenReIssuer;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private HttpServletRequest request;

    @Spy
    private TokenResolver tokenResolver;

    @Mock
    private JwtTokenProvider tokenProvider; //= new JwtTokenProvider(refreshTokenRepository);


    @DisplayName("accessToken 을 재발급한다.")
    @Test
    public void reissueAccessTokenTest() {

        //given
        given(refreshTokenRepository.existsByRefreshToken(any())).willReturn(true);
        given(tokenProvider.validate(any())).willReturn(true);

        //when
        tokenReIssuer.reissueAccessToken(request);

        //then
        then(tokenProvider).should(times(1)).reissueAccessTokenUsing(any());
    }


    @DisplayName("db에 refreshToken 이 없으면, 유효한 토큰이어도 RefreshTokenNotFoundException 발생")
    @Test
    public void refreshTokenNotFoundExceptionTest() {
        //given
        given(refreshTokenRepository.existsByRefreshToken(any())).willReturn(false);
        given(tokenProvider.validate(any())).willReturn(true);

        //when
        assertThrows(RefreshTokenNotFoundException.class,
                () -> tokenReIssuer.reissueAccessToken(request));
    }

    @DisplayName("유효하지 않은 refreshToken 은 InvalidJwtTokenException")
    @Test
    public void invalidRefreshTokenTest() {
        //given
        given(tokenProvider.validate(any())).willReturn(false);

        //when
        assertThrows(InvalidTokenException.class,
                () -> tokenReIssuer.reissueAccessToken(request));
    }
}
