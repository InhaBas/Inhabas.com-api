package com.inhabas.api.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.inhabas.api.security.oauth2.CustomOAuth2User;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RestController
public class LoginController {

    /*
    * Mapping url 을 run-time 으로 설정파일에서 읽어들이기 때문에
    * run-time error 조심해야함!
    * */

    @GetMapping("${login.success-url}")
    @Operation(description = "로그인 성공하여 최종적으로 accessToken, refreshToken 을 발행한다.", hidden = true)
    public ResponseEntity<Object> successLogin(HttpServletRequest request, Principal principal) throws JsonProcessingException {

        OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken) principal;
        String loginIp = ((WebAuthenticationDetails) authentication.getDetails()).getRemoteAddress();
        boolean alreadyJoined = ((CustomOAuth2User) authentication.getPrincipal()).isAlreadyJoined();
        Integer authUserId = ((CustomOAuth2User) authentication.getPrincipal()).getAuthUserId();

        if (alreadyJoined) {
            // jwt 토큰 발행
            //
        }
        else {
            // 회원가입
            // 기존에 회원가입 중이었으면, 회원 정보 넘겨줌.
        }
        // 로그인 이력 남기기.

        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
//        System.out.println(request);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(principal));
        return new ResponseEntity<>("잘 됐다!", HttpStatus.OK);
    }

    @GetMapping("${login.failure-url}")
    @Operation(hidden = true)
    public ResponseEntity<Object> failToLogin() {

        return new ResponseEntity<>("소셜로그인 실패", HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/login/profile")
    @Operation(description = "회원가입 시 프로필을 저장한다.")
    public ResponseEntity<Object> saveProfile() {

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/login/questionnaire")
    @Operation(description = "회원가입에 필요한 질문 리스트를 요청.")
    public ResponseEntity<Object> getQuestionnaire() {

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/login/questionnaire")
    @Operation(description = "회원가입 시 작성한 질문을 저장한다.")
    public ResponseEntity<Object> saveQuestionnaire() {

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
