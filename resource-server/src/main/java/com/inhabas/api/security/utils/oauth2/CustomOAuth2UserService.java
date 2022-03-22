package com.inhabas.api.security.utils.oauth2;

import com.inhabas.api.security.domain.authUser.AuthUser;
import com.inhabas.api.security.domain.authUser.AuthUserDetail;
import com.inhabas.api.security.domain.authUser.AuthUserRepository;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final AuthUserRepository authUserRepository;

    /**
     * OAuth2 인증 후, db에서 회원 정보를 가져옴. 없으면 db에 생성한 후 반환한다.
     * @param userRequest OAuth2 인증 결과가 들어있음. 클라이언트의 attributes 를 가져오기 위해 사용.
     * @return CustomOAuth2User - (클라이언트의 소셜 계정 정보) + (db의 회원 정보)
     * @throws OAuth2AuthenticationException super.loadUser() 에서 발생하는 예외
     */
    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        String provider = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint()
                .getUserNameAttributeName();
        OAuth2Attribute oAuth2Attribute =
                OAuth2Attribute.of(provider, userNameAttributeName, oAuth2User.getAttributes());
        Map<String, Object> memberAttribute = oAuth2Attribute.convertToMap();
        String email = (String) memberAttribute.get("email");

        // 기존 회원 연결하는 로직 필요! 또는 db 옮기는 작업 필요!
        AuthUser loginUser = authUserRepository.findByProviderAndEmail(provider, email)
                .orElse(new AuthUser(provider, email))
                .setLastLoginTime(LocalDateTime.now());
        loginUser = authUserRepository.save(loginUser);

        return new CustomOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                memberAttribute, "email", AuthUserDetail.convert(loginUser));
    }


    /**
     * OAuth2 인증 결과로부터 이름, 이메일, 프로필 사진 정보를 일관된 메소드로 뽑아오기 위함.
     */
    @ToString
    @Builder(access = AccessLevel.PRIVATE)
    @Getter
    private static class OAuth2Attribute {
        private Map<String, Object> attributes;
        private String attributeKey;
        private String email;
        private String name;
        private String picture;

        static OAuth2Attribute of(String provider, String attributeKey,
                                  Map<String, Object> attributes) {
            switch (provider) {
                case "google":
                    return ofGoogle(attributeKey, attributes);
                case "kakao":
                    return ofKakao("email", attributes);
                case "naver":
                    return ofNaver("id", attributes);
                default:
                    throw new RuntimeException();
            }
        }

        private static OAuth2Attribute ofGoogle(String attributeKey,
                                                Map<String, Object> attributes) {
            return OAuth2Attribute.builder()
                    .name((String) attributes.get("name"))
                    .email((String) attributes.get("email"))
                    .picture((String) attributes.get("picture"))
                    .attributes(attributes)
                    .attributeKey(attributeKey)
                    .build();
        }

        @SuppressWarnings("unchecked")
        private static OAuth2Attribute ofKakao(String attributeKey,
                                               Map<String, Object> attributes) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");

            return OAuth2Attribute.builder()
                    .name((String) kakaoProfile.get("nickname"))
                    .email((String) kakaoAccount.get("email"))
                    .picture((String)kakaoProfile.get("profile_image_url"))
                    .attributes(kakaoAccount)
                    .attributeKey(attributeKey)
                    .build();
        }

        @SuppressWarnings("unchecked")
        private static OAuth2Attribute ofNaver(String attributeKey,
                                               Map<String, Object> attributes) {
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");

            return OAuth2Attribute.builder()
                    .name((String) response.get("name"))
                    .email((String) response.get("email"))
                    .picture((String) response.get("profile_image"))
                    .attributes(response)
                    .attributeKey(attributeKey)
                    .build();
        }

        Map<String, Object> convertToMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("id", attributeKey);
            map.put("key", attributeKey);
            map.put("name", name);
            map.put("email", email);
            map.put("picture", picture);

            return map;
        }
    }
}
