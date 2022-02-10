package com.inhabas.api.security.oauth2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;

@Configuration
public class OAuth2Config {

    @Bean
    public OAuth2AuthorizedClientRepository OAuth2AuthorizedClientRepository() {
        /* 인증된 oauth2 정보가 세션에 저장됨.
         spring security 의 default 설정이 in-memory 방식이라 변경했음.
         https://github.com/InhaBas/Inhabas.com-api/issues/3
         */
        return new HttpSessionOAuth2AuthorizedClientRepository();
    }
}
