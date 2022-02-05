package com.inhabas.api.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.inhabas.api.security.jwtUtils.JwtTokenDto;
import com.inhabas.api.security.jwtUtils.JwtTokenProvider;
import com.inhabas.api.security.oauth2.CustomOAuth2User;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RequiredArgsConstructor
@RestController
public class LoginController {

    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("${login.success-url}")
    @Operation(description = "로그인 성공하여 최종적으로 accessToken, refreshToken 을 발행한다.", hidden = true)
    public ResponseEntity<?> successLogin(HttpServletRequest request, Principal principal) {

        OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken) principal;
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        String loginIp = ((WebAuthenticationDetails) authentication.getDetails()).getRemoteAddress();
//        String provider = authentication.getAuthorizedClientRegistrationId();
//        String email = (String) oAuth2User.getAttributes().get("email");
        boolean alreadyJoined = oAuth2User.isAlreadyJoined();
        Integer authUserId = oAuth2User.getAuthUserId();

        if (alreadyJoined) {

            // 유저 권한 들고오기
            JwtTokenDto jwtToken = jwtTokenProvider.createJwtToken(authUserId, null, null);
            // refresh token db 에 저장하

            return ResponseEntity.ok(jwtToken);
        }
        else {
            // 회원가입 해야됨.

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        // 로그인 이력 남기기.

//        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
////        System.out.println(request);
//        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(principal));
//        return new ResponseEntity<>("잘 됐다!", HttpStatus.OK);요
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
