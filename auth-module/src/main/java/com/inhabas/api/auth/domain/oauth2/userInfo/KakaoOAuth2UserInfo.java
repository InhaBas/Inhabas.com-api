package com.inhabas.api.auth.domain.oauth2.userInfo;

import java.util.Map;


public class KakaoOAuth2UserInfo extends OAuth2UserInfo {

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "nickname";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_IMAGE_URL = "profile_image";
    private static final String KEY_EXTRA_DATA = "kakao_account";

    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {

        return (String) attributes.get(KEY_ID);
    }

    @Override
    public String getName() {

        return (String) this.getProfile().get(KEY_NAME);
    }

    @Override
    public String getEmail() {

        return (String) this.getExtraData().get(KEY_EMAIL);
    }

    @Override
    public String getImageUrl() {

        return (String) this.getProfile().get(KEY_IMAGE_URL);
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public Map<String, Object> getExtraData() {

        return (Map<String, Object>) attributes.get(KEY_EXTRA_DATA);
    }

    @SuppressWarnings({"unchecked"})
    private Map<String, Object> getProfile() {

        return (Map<String, Object>) this.getExtraData().get("profile");
    }
}

/*  attribute 는 아래와 같은 형식
{
    "id" : (10자리 숫자),
    "connected_at" : "2022-01-28T14:26:21Z",
    "properties" : {
        "nickname" : "동현 유",
        "profile_image" : "http://k.kakaocdn.net/dn/.../img_640x640.jpg",
        "thumbnail_image" : "http://k.kakaocdn.net/dn/.../img_110x110.jpg"
    },
    "kakao_account" : {
        "profile_nickname_needs_agreement" : false,
        "profile_image_needs_agreement" : false,
        "profile" : {
            "nickname" : "동현 유",
            "thumbnail_image_url" : "http://k.kakaocdn.net/dn/.../img_110x110.jpg"
            "profile_image_url" : "http://k.kakaocdn.net/dn/.../img_640x640.jpg",
            "is_default_image" : false
        },
        "has_email" : true,
        "email_needs_agreement" : false,
        "is_email_valid" : true,
        "is_email_verified" : true,
        "email" : "my@gmail.com",
        "has_gender" : true,
        "gender_needs_agreement" : false,
        "gender" : "male"
     }
}*/
