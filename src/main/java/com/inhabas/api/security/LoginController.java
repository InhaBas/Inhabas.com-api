package com.inhabas.api.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.inhabas.api.security.domain.RefreshToken;
import com.inhabas.api.security.domain.RefreshTokenRepository;
import com.inhabas.api.security.jwtUtils.JwtTokenDto;
import com.inhabas.api.security.jwtUtils.JwtTokenProvider;
import com.inhabas.api.security.oauth2.CustomOAuth2User;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RequiredArgsConstructor
@RestController
public class LoginController {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @GetMapping("${login.success-url}")
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
            JwtTokenDto jwtToken = jwtTokenProvider
                    .createJwtToken(authUserId, new SimpleGrantedAuthority("ROLE_MEMBER"), null);
            refreshTokenRepository.save(new RefreshToken(jwtToken.getRefreshToken()));

            return ResponseEntity.ok(jwtToken);
        }
        else {
            return new ResponseEntity<>("회원가입 해야됨.", HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("${login.failure-url}")
    @Operation(hidden = true)
    public ResponseEntity<?> failToLogin() {

        return new ResponseEntity<>("소셜로그인 실패", HttpStatus.UNAUTHORIZED);
    }

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
