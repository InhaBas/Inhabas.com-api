package com.inhabas.api.domain.member.domain.entity;


import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.IbasInformation;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.SchoolInformation;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.StudentId;
import com.inhabas.api.auth.domain.oauth2.userInfo.GoogleOAuth2UserInfo;
import com.inhabas.api.auth.domain.oauth2.userInfo.OAuth2UserInfo;

import java.util.HashMap;
import java.util.Map;

import static com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.MemberType.UNDERGRADUATE;
import static com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role.*;

public class MemberTest {

    public static Member signingUpMember1() {

        Map<String, Object> attributes = new HashMap<>() {{
            put("provider", "GOOGLE");
            put("sub", "1249846925629348");
            put("picture", "/static/image.jpg");
            put("email", "my@gmail.com");
            put("name", "유동현");
            put("locale", "ko");
        }};
        OAuth2UserInfo user = new GoogleOAuth2UserInfo(attributes);
        Member member = new Member(user);
        member.setRole(SIGNING_UP);

        return member;
    }


    public static Member signingUpMember2() {

        Map<String, Object> attributes = new HashMap<>() {{
            put("provider", "GOOGLE");
            put("sub", "3232322332323223");
            put("picture", "/static/image.jpg");
            put("email", "my@gmail.com");
            put("name", "조승현");
            put("locale", "ko");
        }};
        OAuth2UserInfo user = new GoogleOAuth2UserInfo(attributes);
        Member member = new Member(user);
        member.setRole(SIGNING_UP);

        return member;
    }

    public static Member signingUpMemberAfterProfile() {

        Map<String, Object> attributes = new HashMap<>() {{
            put("provider", "GOOGLE");
            put("sub", "3232322332323223");
            put("picture", "/static/image.jpg");
            put("email", "my@gmail.com");
            put("name", "조승현");
            put("locale", "ko");
        }};
        OAuth2UserInfo user = new GoogleOAuth2UserInfo(attributes);
        Member member = new Member(user);
        member.setRole(SIGNING_UP);

        // 회원정보 저장
        member.setStudentId("12171234");
        member.setPhone("010-1111-1111");
        member.setName("유동현");
        member.setSchoolInformation(new SchoolInformation("컴퓨터공학과", 1, 1, UNDERGRADUATE));
        return member;
    }

    public static Member basicMember1() {
        return new Member(
                new StudentId("12171234"), "유동현", "010-1111-1111", "my@gmail.com", ""
                , SchoolInformation.ofUnderGraduate("건축공학과", 3)
                , new IbasInformation(BASIC));
    }

    public static Member basicMember2() {
        return new Member(
                new StudentId("12114321"), "김민겸", "010-2222-2222", "my@gmail.com", ""
                , SchoolInformation.ofUnderGraduate("경영학과", 2)
                , new IbasInformation(BASIC));
    }

    public static Member deactivatedMember() {

        Map<String, Object> attributes = new HashMap<>() {{
            put("provider", "GOOGLE");
            put("sub", "1249846925629348");
            put("picture", "/static/image.jpg");
            put("email", "my@gmail.com");
            put("name", "유동현");
            put("locale", "ko");
        }};
        OAuth2UserInfo user = new GoogleOAuth2UserInfo(attributes);
        Member member = new Member(user);
        member.setEmail("my@gmail.com");
        member.setName("유동현");

        return new Member(
                new StudentId("12171707"), "김미승인", "010-1111-1111", "my@gmail.com", ""
                , SchoolInformation.ofUnderGraduate("컴퓨터공학과", 2)
                , new IbasInformation(DEACTIVATED));

    }

    public static Member notapprovedMember() {

        Map<String, Object> attributes = new HashMap<>() {{
            put("provider", "GOOGLE");
            put("sub", "1249846925629348");
            put("picture", "/static/image.jpg");
            put("email", "my@gmail.com");
            put("name", "유동현");
            put("locale", "ko");
        }};
        OAuth2UserInfo user = new GoogleOAuth2UserInfo(attributes);
        Member member = new Member(user);
        member.setEmail("my@gmail.com");
        member.setName("유동현");

        return new Member(
                new StudentId("12171707"), "김미승인", "010-1111-1111", "my@gmail.com", ""
                , SchoolInformation.ofUnderGraduate("컴퓨터공학과", 2)
                , new IbasInformation(NOT_APPROVED));

    }


    public static Member getTestBasicMember(String id) {
        return new Member(
                new StudentId(id), "유동현", "010-1111-1111", "my@gmail.com", ""
                , SchoolInformation.ofUnderGraduate("건축공학과", 3)
                , new IbasInformation(BASIC));
    }

    public static Member getTestBasicMember(String id, String phoneNumber) {
        return new Member(
                new StudentId(id), "유동현", phoneNumber, "my@gmail.com", ""
                , SchoolInformation.ofUnderGraduate("건축공학과", 3)
                , new IbasInformation(BASIC));
    }


}
