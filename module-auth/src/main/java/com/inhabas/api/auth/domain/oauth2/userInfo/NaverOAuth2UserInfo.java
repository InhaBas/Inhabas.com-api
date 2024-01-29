package com.inhabas.api.auth.domain.oauth2.userInfo;

import java.util.Map;
import java.util.Objects;

import com.inhabas.api.auth.domain.oauth2.OAuth2Provider;

public class NaverOAuth2UserInfo extends OAuth2UserInfo {

  private static final String KEY_ID = "id";
  private static final String KEY_NAME = "name";
  private static final String KEY_EMAIL = "email";
  private static final String KEY_IMAGE_URL = "profile_image";
  private static final String KEY_EXTRA_DATA = "response";

  public NaverOAuth2UserInfo(Map<String, Object> attributes) {
    super(OAuth2Provider.NAVER, attributes);
  }

  @Override
  public String getId() {
    return Objects.toString(this.getExtraData().get(KEY_ID));
  }

  @Override
  public String getName() {
    return (String) this.getExtraData().get(KEY_NAME);
  }

  @Override
  public String getEmail() {
    return (String) this.getExtraData().get(KEY_EMAIL);
  }

  @Override
  public String getImageUrl() {
    return (String) this.getExtraData().get(KEY_IMAGE_URL);
  }

  @SuppressWarnings({"unchecked"})
  @Override
  public Map<String, Object> getExtraData() {
    return (Map<String, Object>) this.attributes.get(KEY_EXTRA_DATA);
  }
}
/*  attribute 는 아래와 같은 형식
{
    "resultcode" : "00",
    "message" : "success",
    "response" : {
        "id" : " INT64 규격의 숫자 (최대 20자)",
        "profile_image" : "https://ssl.pstatic.net/static/pwe/address/img_profile.png",
        "gender" : "M",
        "email" : "my@naver.com",
        "mobile" : "010-1234-1234",
        "mobile_e164" : "+821012341234",
        "name" : "홍길동"
    }
}*/
