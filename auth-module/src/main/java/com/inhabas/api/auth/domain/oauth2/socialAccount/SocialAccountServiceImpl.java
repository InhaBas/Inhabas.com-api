package com.inhabas.api.auth.domain.oauth2.socialAccount;

import com.inhabas.api.auth.domain.oauth2.socialAccount.type.UID;
import com.inhabas.api.auth.domain.oauth2.userInfo.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SocialAccountServiceImpl implements SocialAccountService {

    private final SocialAccountRepository socialAccountRepository;

    @Override
    public void updateSocialAccountInfo(OAuth2UserInfo oAuth2UserInfo) {

        SocialAccount socialAccount = socialAccountRepository
                .findByUidAndProvider(new UID(oAuth2UserInfo.getId()), oAuth2UserInfo.getProvider())
                .orElse(new SocialAccount(oAuth2UserInfo))
                .setLastLoginTime(LocalDateTime.now());

        socialAccountRepository.save(socialAccount);
    }
}
