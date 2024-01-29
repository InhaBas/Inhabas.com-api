package com.inhabas.api.auth.domain.token.jwtUtils.refreshToken;

import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(columnDefinition = "TEXT")
  private String refreshToken;

  private LocalDateTime created;

  public RefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
    this.created = LocalDateTime.now();
  }
}
