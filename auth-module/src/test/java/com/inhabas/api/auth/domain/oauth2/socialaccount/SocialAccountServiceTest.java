package com.inhabas.api.auth.domain.oauth2.socialaccount;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inhabas.api.auth.domain.oauth2.OAuth2Provider;
import com.inhabas.api.auth.domain.oauth2.userInfo.GoogleOAuth2UserInfo;
import com.inhabas.api.auth.domain.oauth2.userInfo.OAuth2UserInfo;
import com.inhabas.api.auth.domain.oauth2.socialAccount.SocialAccount;
import com.inhabas.api.auth.domain.oauth2.socialAccount.SocialAccountRepository;
import com.inhabas.api.auth.domain.oauth2.socialAccount.SocialAccountServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class SocialAccountServiceTest {

    @InjectMocks
    private SocialAccountServiceImpl socialAccountService;

    @Mock
    private SocialAccountRepository socialAccountRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @DisplayName("처음 소셜로그인 시도할 때, 새로운 소셜계정 객체를 생성한다.")
    @Test
    @SuppressWarnings({"unchecked"})
    public void saveSocialAccountInfo() throws JsonProcessingException {
        //given
        Map<String, Object> attributes = objectMapper.readValue("{\n" +
                "    \"sub\" : \"1234567889\",\n" +
                "    \"name\" : \"유동현\",\n" +
                "    \"given_name\" : \"동현\",\n" +
                "    \"family_name\" : \"유\",\n" +
                "    \"picture\" : \"https://lh3.googleusercontent.com/a/AATXAJzeE07A14_4sjavMyRvRpuG7gcVa5O8imNA37pe=s96-c\",\n" +
                "    \"email\" : \"my@gmail.com\",\n" +
                "    \"email_verified\" : true,\n" +
                "    \"locale\" : \"ko\"\n" +
                "}", Map.class);
        OAuth2UserInfo userInfo = new GoogleOAuth2UserInfo(attributes);

        given(socialAccountRepository.findByUidAndProvider(any(), any()))
                .willReturn(Optional.empty()); // 소셜로그인이 처음이다.

        //when
        socialAccountService.updateSocialAccountInfo(userInfo);

        //then
        then(socialAccountRepository).should(times(1)).findByUidAndProvider(any(), any());
        then(socialAccountRepository).should(times(1)).save(any());
    }

    @DisplayName("소셜로그인 시도가 첫번째가 아니면, 마지막 로그인 시간만 수정한다.")
    @Test
    @SuppressWarnings({"unchecked"})
    public void updateSocialAccountInfo() throws JsonProcessingException {
        //given
        String extraData = "{\n" +
                "    \"sub\" : \"1234567889\",\n" +
                "    \"name\" : \"유동현\",\n" +
                "    \"given_name\" : \"동현\",\n" +
                "    \"family_name\" : \"유\",\n" +
                "    \"picture\" : \"https://lh3.googleusercontent.com/a/AATXAJzeE07A14_4sjavMyRvRpuG7gcVa5O8imNA37pe=s96-c\",\n" +
                "    \"email\" : \"my@gmail.com\",\n" +
                "    \"email_verified\" : true,\n" +
                "    \"locale\" : \"ko\"\n" +
                "}";
        Map<String, Object> attributes = objectMapper.readValue(extraData, Map.class);
        OAuth2UserInfo userInfo = new GoogleOAuth2UserInfo(attributes);

        SocialAccount existAccount = new SocialAccount(OAuth2Provider.GOOGLE, userInfo.getId(), LocalDateTime.of(2020, 1, 1, 12, 0, 0),
                LocalDateTime.of(2020, 1, 1, 13, 0, 0), extraData);
        given(socialAccountRepository.findByUidAndProvider(any(), any()))
                .willReturn(Optional.of(existAccount)); // 소셜로그인을 전에 한 적이 있다.

        //when
        socialAccountService.updateSocialAccountInfo(userInfo);

        //then
        then(socialAccountRepository).should(times(1)).findByUidAndProvider(any(), any());
        then(socialAccountRepository).should(times(1)).save(any());
    }
}
