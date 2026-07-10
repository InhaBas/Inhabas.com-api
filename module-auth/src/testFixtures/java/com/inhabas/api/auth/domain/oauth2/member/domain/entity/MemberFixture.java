package com.inhabas.api.auth.domain.oauth2.member.domain.entity;

import static com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.MemberType.UNDERGRADUATE;
import static com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role.*;

import java.util.HashMap;
import java.util.Map;

import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.IbasInformation;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.SchoolInformation;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.StudentId;
import com.inhabas.api.auth.domain.oauth2.userInfo.GoogleOAuth2UserInfo;
import com.inhabas.api.auth.domain.oauth2.userInfo.OAuth2UserInfo;

/** 테스트에서 공용으로 사용하는 Member 픽스처 정적 팩토리. */
public class MemberFixture {

  private static Map<String, Object> googleAttributes(String sub, String email, String name) {
    Map<String, Object> attributes = new HashMap<>();
    attributes.put("provider", "GOOGLE");
    attributes.put("sub", sub);
    attributes.put("picture", "/static/image.jpg");
    attributes.put("email", email);
    attributes.put("name", name);
    attributes.put("locale", "ko");
    return attributes;
  }

  private static Member afterSignUpMember(Role role) {
    OAuth2UserInfo user =
        new GoogleOAuth2UserInfo(googleAttributes("3232322332323223", "my@gmail.com", "조승현"));
    Member member = new Member(user);
    member.setRole(role);

    // 회원정보 저장
    member.setStudentId("12171707");
    member.setPhone("010-1111-1111");
    member.setName("조승현");
    member.setSchoolInformation(new SchoolInformation("컴퓨터공학과", 1, 1, UNDERGRADUATE));
    return member;
  }

  private static Member basicMemberOf(String sub, String email, String name) {
    OAuth2UserInfo user = new GoogleOAuth2UserInfo(googleAttributes(sub, email, name));
    Member member = new Member(user);
    member.setRole(BASIC);

    // 회원정보 저장
    member.setStudentId("12171707");
    member.setPhone("010-1111-1111");
    member.setName(name);
    member.setSchoolInformation(new SchoolInformation("컴퓨터공학과", 1, 1, UNDERGRADUATE));
    return member;
  }

  private static Member signingUpMemberOf(String sub, String email, String name) {
    OAuth2UserInfo user = new GoogleOAuth2UserInfo(googleAttributes(sub, email, name));
    Member member = new Member(user);
    member.setRole(SIGNING_UP);
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

  public static Member basicMember1() {
    return basicMemberOf("121212121212112", "yu@gmail.com", "유동현");
  }

  public static Member basicMember2() {
    return basicMemberOf("3232322332323223", "jo@gmail.com", "조승현");
  }

  public static Member signingUpMember1() {
    return signingUpMemberOf("1249846925629348", "my@gmail.com", "유동현");
  }

  public static Member signingUpMember2() {
    return signingUpMemberOf("3232322332323223", "my2@gmail.com", "조승현");
  }

  public static Member getTestBasicMember(String id) {
    return getTestBasicMember(id, "010-1111-1111");
  }

  public static Member getTestBasicMember(String id, String phoneNumber) {
    return new Member(
        new StudentId(id),
        "유동현",
        phoneNumber,
        "my@gmail.com",
        "",
        SchoolInformation.ofUnderGraduate("건축공학과", 3),
        new IbasInformation(BASIC));
  }
}
