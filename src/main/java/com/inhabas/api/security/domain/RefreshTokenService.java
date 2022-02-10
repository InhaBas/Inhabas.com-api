package com.inhabas.api.security.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void save(RefreshToken refreshToken) {
        refreshTokenRepository.save(refreshToken);
    }

    @Transactional(readOnly = true)
    public boolean DoesExist(String refreshTokenString) {
        return refreshTokenRepository.findByRefreshToken(refreshTokenString)
                .isPresent();
    }
}
