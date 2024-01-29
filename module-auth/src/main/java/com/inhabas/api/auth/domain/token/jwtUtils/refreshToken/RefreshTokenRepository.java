package com.inhabas.api.auth.domain.token.jwtUtils.refreshToken;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

  boolean existsByRefreshToken(String refreshToken);
}
