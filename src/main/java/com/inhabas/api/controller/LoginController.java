package com.inhabas.api.controller;

import com.inhabas.api.security.domain.RefreshToken;
import com.inhabas.api.security.domain.RefreshTokenService;
import com.inhabas.api.security.jwtUtils.InvalidJwtTokenException;
import com.inhabas.api.security.jwtUtils.JwtTokenDto;
import com.inhabas.api.security.jwtUtils.JwtTokenProvider;
import com.inhabas.api.security.oauth2.CustomOAuth2User;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class LoginController {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

    /* token authentication */

    @GetMapping("${authenticate.oauth2-success-handle-url}")
    @Operation(description = "로그인 성공하여 최종적으로 accessToken, refreshToken 을 발행한다.", hidden = true)
    public ResponseEntity<?> successLogin(HttpServletRequest request, Principal principal) {

        OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken) principal;
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        boolean alreadyJoined = oAuth2User.isAlreadyJoined();
        Integer authUserId = oAuth2User.getAuthUserId();

        if (alreadyJoined) {

            /* 유저 권한 들고오기 (추후 작업이 필요함.)
            - role 은 (미승인회원, 일반회원, 교수, 회장단, 회장) 과 같이 수직구조의 권한 => 상대적으로 덜 변함. => enum 타입
            - team 은 (총무팀, 운영팀, 기획팀, IT팀, 회계) 등 과 같은 수평구조의 권한 => 시간에 따라 더 변하기 쉬움 => db 연동
            */
            JwtTokenDto jwtToken = jwtTokenProvider.createJwtToken(authUserId, "ROLE_MEMBER", null);
            refreshTokenService.save(new RefreshToken(jwtToken.getRefreshToken()));

            request.getSession().invalidate(); // 프론트 단에서 브라우저 쿠키 JSESSIONID, XSRF-TOKEN 지우는 게 좋을 듯. 상관없긴 한디.

            return ResponseEntity.ok(jwtToken);
        }
        else {
            return new ResponseEntity<>("회원가입 해야됨.", HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("${authenticate.invalid-jwt-token-handle-url}")
    @Operation(description = "토큰 유효성 검사에 실패했다.", hidden = true)
    public ResponseEntity<Map<String, String>> invalidToken() {
        Map<String, String> message = new HashMap<>() {{
            put("message","invalid_token");
        }};
        return new ResponseEntity<>(message, HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("${authenticate.oauth2-failure-handle-url}")
    @Operation(hidden = true)
    public ResponseEntity<?> failToLogin() {
        Map<String, String> message = new HashMap<>() {{
            put("message","fail_to_social_login");
        }};
        return new ResponseEntity<>(message, HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("${authenticate.reissue-access-token-url}")
    @Operation(description = "access token 재발급을 요청한다.")
    public ResponseEntity<JwtTokenDto> reissueAccessToken(HttpServletRequest request) {
        String refreshToken = jwtTokenProvider.resolveToken(request);

        try {
            if (refreshTokenService.DoesExist(refreshToken)) {
                JwtTokenDto newAccessToken = jwtTokenProvider.reissueAccessTokenUsing(refreshToken);
                return ResponseEntity.ok(newAccessToken);
            }
            else {
                throw new InvalidJwtTokenException();
            }
        } catch (InvalidJwtTokenException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    /* join new member */

    @PostMapping("/login/profile")
    @Operation(description = "회원가입 시 프로필을 저장한다.")
    public ResponseEntity<?> saveProfile() {

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/login/questionnaire")
    @Operation(description = "회원가입에 필요한 질문 리스트를 요청.")
    public ResponseEntity<?> getQuestionnaire() {

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/login/questionnaire")
    @Operation(description = "회원가입 시 작성한 질문을 저장한다.")
    public ResponseEntity<?> saveQuestionnaire() {

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
