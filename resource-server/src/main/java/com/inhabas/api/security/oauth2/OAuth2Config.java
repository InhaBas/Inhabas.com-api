package com.inhabas.api.security.oauth2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;

@Configuration
public class OAuth2Config {

    /**
     * 인증된 oauth2 정보를 세션에 저장하도록 함.
     * spring security 의 default 설정이 in-memory 방식이라, session 방식으로 변경했음.
     * @see <a href="https://github.com/InhaBas/Inhabas.com-api/issues/3#issuecomment-1028777973">issue-#3</a>
     * @return HttpSessionOAuth2AuthorizedClientRepository
     */
    @Bean
    public OAuth2AuthorizedClientRepository OAuth2AuthorizedClientRepository() {

        return new HttpSessionOAuth2AuthorizedClientRepository();
    }
}
