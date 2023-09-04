package com.inhabas.api.domain.member.domain;

import com.inhabas.api.domain.member.domain.entity.Member;
import com.inhabas.api.domain.member.domain.valueObject.IbasInformation;
import com.inhabas.api.domain.member.domain.valueObject.SchoolInformation;
import com.inhabas.api.domain.member.domain.valueObject.*;

import static com.inhabas.api.domain.member.domain.valueObject.Role.*;

public class MemberTest {
    
    public static Member chiefMember() {
        return new Member(
                new MemberId(12171707), "김회장", "010-1111-1111", "my@gmail.com", ""
                , SchoolInformation.ofUnderGraduate("컴퓨터공학과", 2)
                , new IbasInformation(CHIEF));
    }

    public static Member executivesMember() {
        return new Member(
                new MemberId(12201122), "박임원", "010-2222-2222", "my@gmail.com", ""
                , SchoolInformation.ofUnderGraduate("컴퓨터공학과", 2)
                , new IbasInformation(EXECUTIVES));
    }
    
    public static Member secretaryMember() {
        return new Member(
                new MemberId(12219882), "이총무", "010-3333-3333", "my@gmail.com", ""
                , SchoolInformation.ofUnderGraduate("컴퓨터공학과", 2)
                , new IbasInformation(SECRETARY));
    }
    
    public static Member basicMember1() {
        return new Member(
                new MemberId(12171234), "유동현", "010-1111-1111", "my@gmail.com", ""
                , SchoolInformation.ofUnderGraduate("건축공학과", 3)
                , new IbasInformation(BASIC));
    }

    public static Member basicMember2() {
        return new Member(
                new MemberId(12114321), "김민겸", "010-2222-2222", "my@gmail.com", ""
                , SchoolInformation.ofUnderGraduate("경영학과", 2)
                , new IbasInformation(BASIC));
    }

    public static Member deactivatedMember() {
        return new Member(
                new MemberId(12171707), "최비활", "010-1111-1111", "my@gmail.com", ""
                , SchoolInformation.ofUnderGraduate("컴퓨터공학과", 2)
                , new IbasInformation(DEACTIVATED));
    }

    public static Member notapprovedMember() {
        return new Member(
                new MemberId(12171707), "김미승인", "010-1111-1111", "my@gmail.com", ""
                , SchoolInformation.ofUnderGraduate("컴퓨터공학과", 2)
                , new IbasInformation(NOT_APPROVED));
    }


    public static Member getTestBasicMember(Integer id) {
        return new Member(
                new MemberId(id), "유동현", "010-1111-1111", "my@gmail.com", ""
                , SchoolInformation.ofUnderGraduate("건축공학과", 3)
                , new IbasInformation(BASIC));
    }

    public static Member getTestBasicMember(Integer id, String phoneNumber) {
        return new Member(
                new MemberId(id), "유동현", phoneNumber, "my@gmail.com", ""
                , SchoolInformation.ofUnderGraduate("건축공학과", 3)
                , new IbasInformation(BASIC));
    }


}
