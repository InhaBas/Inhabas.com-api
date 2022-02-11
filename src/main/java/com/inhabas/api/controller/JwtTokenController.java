package com.inhabas.api.controller;

import com.inhabas.api.security.domain.RefreshTokenService;
import com.inhabas.api.security.jwtUtils.InvalidJwtTokenException;
import com.inhabas.api.security.jwtUtils.TokenDto;
import com.inhabas.api.security.jwtUtils.TokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class JwtTokenController {

    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;

    @GetMapping("${authenticate.invalid-jwt-token-handle-url}")
    @Operation(description = "토큰 유효성 검사에 실패했다.", hidden = true)
    public ResponseEntity<Map<String, String>> invalidToken() {
        Map<String, String> message = new HashMap<>() {{
            put("message","invalid_token");
        }};
        return new ResponseEntity<>(message, HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("${authenticate.reissue-access-token-url}")
    @Operation(description = "access token 재발급을 요청한다.")
    public ResponseEntity<TokenDto> reissueAccessToken(HttpServletRequest request) {
        String refreshToken = tokenProvider.resolveToken(request);

        try {
            if (refreshTokenService.DoesExist(refreshToken)) {
                TokenDto newAccessToken = tokenProvider.reissueAccessTokenUsing(refreshToken);
                return ResponseEntity.ok(newAccessToken);
            }
            else {
                throw new InvalidJwtTokenException();
            }
        } catch (InvalidJwtTokenException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
