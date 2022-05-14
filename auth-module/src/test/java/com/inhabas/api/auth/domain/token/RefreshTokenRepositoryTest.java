package com.inhabas.api.auth.domain.token;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class RefreshTokenRepositoryTest {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @DisplayName("리프레시 토큰이 db 에 존재한다.")
    @Test
    public void existRefreshTokenTest() {
        //given
        String jws = "header.claims.signature";
        RefreshToken refreshToken = new RefreshToken(jws);

        refreshTokenRepository.save(refreshToken);

        //then
        Assertions.assertThat(refreshTokenRepository.existsByRefreshToken(jws)).isTrue();
    }

    @DisplayName("리프레시 토큰이 db 에 존재하지 않는다.")
    @Test
    public void notExistRefreshTokenTest() {
        //given
        String jws = "header.claims.signature";

        //then
        Assertions.assertThat(refreshTokenRepository.existsByRefreshToken(jws)).isFalse();
    }
}
