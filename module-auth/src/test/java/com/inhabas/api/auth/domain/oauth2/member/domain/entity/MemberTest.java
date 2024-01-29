package com.inhabas.api.auth.domain.oauth2.member.domain.entity;

import static com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.MemberType.UNDERGRADUATE;
import static com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role.*;

import java.util.HashMap;
import java.util.Map;

import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.SchoolInformation;
import com.inhabas.api.auth.domain.oauth2.userInfo.GoogleOAuth2UserInfo;
import com.inhabas.api.auth.domain.oauth2.userInfo.OAuth2UserInfo;

public class MemberTest {

  private static Member afterSignUpMember(Role role) {

    Map<String, Object> attributes =
        new HashMap<>() {
          {
            put("provider", "GOOGLE");
            put("sub", "3232322332323223");
            put("picture", "/static/image.jpg");
            put("email", "my@gmail.com");
            put("name", "조승현");
            put("locale", "ko");
          }
        };
    OAuth2UserInfo user = new GoogleOAuth2UserInfo(attributes);
    Member member = new Member(user);
    member.setRole(role);

    // 회원정보 저장
    member.setStudentId("12171707");
    member.setPhone("010-1111-1111");
    member.setName("조승현");
    member.setSchoolInformation(new SchoolInformation("컴퓨터공학과", 1, 1, UNDERGRADUATE));
    return member;
  }

  public static Member chiefMember() {
    return afterSignUpMember(CHIEF);
  }

  public static Member executivesMember() {
    return afterSignUpMember(EXECUTIVES);
  }

  public static Member secretaryMember() {
    return afterSignUpMember(SECRETARY);
  }

  public static Member basicMember() {
    return afterSignUpMember(BASIC);
  }

  public static Member deactivatedMember() {
    return afterSignUpMember(DEACTIVATED);
  }

  public static Member notapprovedMember() {
    return afterSignUpMember(NOT_APPROVED);
  }

  public static Member signingUpMemberAfterProfile() {
    return afterSignUpMember(SIGNING_UP);
  }

  public static Member signingUpMember1() {

    Map<String, Object> attributes =
        new HashMap<>() {
          {
            put("provider", "GOOGLE");
            put("sub", "1249846925629348");
            put("picture", "/static/image.jpg");
            put("email", "my@gmail.com");
            put("name", "유동현");
            put("locale", "ko");
          }
        };
    OAuth2UserInfo user = new GoogleOAuth2UserInfo(attributes);
    Member member = new Member(user);
    member.setRole(SIGNING_UP);

    return member;
  }

  public static Member signingUpMember2() {

    Map<String, Object> attributes =
        new HashMap<>() {
          {
            put("provider", "GOOGLE");
            put("sub", "3232322332323223");
            put("picture", "/static/image.jpg");
            put("email", "my2@gmail.com");
            put("name", "조승현");
            put("locale", "ko");
          }
        };
    OAuth2UserInfo user = new GoogleOAuth2UserInfo(attributes);
    Member member = new Member(user);
    member.setRole(SIGNING_UP);

    return member;
  }
}
