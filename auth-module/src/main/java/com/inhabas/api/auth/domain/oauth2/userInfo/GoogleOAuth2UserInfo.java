package com.inhabas.api.auth.domain.oauth2.userInfo;

import java.util.Map;

public class GoogleOAuth2UserInfo extends OAuth2UserInfo {

    private static final String KEY_ID = "sub";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_IMAGE_URL = "picture";

    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return (String) this.attributes.get(KEY_ID);
    }

    @Override
    public String getName() {
        return (String) this.attributes.get(KEY_NAME);
    }

    @Override
    public String getEmail() {
        return (String) this.attributes.get(KEY_EMAIL);
    }

    @Override
    public String getImageUrl() {
        return (String) this.attributes.get(KEY_IMAGE_URL);
    }

    @Override
    public Map<String, Object> getExtraData() {
        return this.attributes;
    }
}
/* attribute 는 아래와 같은 형식
{
    "sub" : "111629839257302804853",
    "name" : "유동현",
    "given_name" : "동현",
    "family_name" : "유",
    "picture" : "https://lh3.googleusercontent.com/a/AATXAJzeE07A14_4sjavMyRvRpuG7gcVa5O8imNA37pe=s96-c",
    "email" : "ydh9516@gmail.com",
    "email_verified" : true,
    "locale" : "ko"
}*/
