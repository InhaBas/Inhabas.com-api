package com.inhabas.api.auth.domain.oauth2.socialAccount;

import com.inhabas.api.auth.domain.oauth2.userInfo.OAuth2UserInfo;

public interface SocialAccountService {

    /**
     * OAuth2 인증이 정상적으로 완료된 {@code SocialAccount} 의 정보를 db에 저장한다.
     * @param oAuth2UserInfo 인증 완료 후 가공된 소셜계정 정보
     */
    void updateSocialAccountInfo(OAuth2UserInfo oAuth2UserInfo);
}
