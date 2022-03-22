package com.inhabas.api.domain;

import com.inhabas.api.domain.member.*;
import com.inhabas.api.domain.member.type.IbasInformation;
import com.inhabas.api.domain.member.type.SchoolInformation;
import com.inhabas.api.domain.member.type.wrapper.*;

public class MemberTest {
    public static final Member MEMBER1 = new Member(
            12171234, "유동현", "010-1111-1111", "my@gmail.com", ""
            , SchoolInformation.ofUnderGraduate("건축공학과", 3)
            , new IbasInformation(Role.BASIC_MEMBER));

    public static final Member MEMBER2 = new Member(
            12114321, "김민겸", "010-2222-2222", "my@gmail.com", ""
            , SchoolInformation.ofUnderGraduate("경영학과", 2)
            , new IbasInformation(Role.BASIC_MEMBER));

    public static Member getTestMember(Integer id) {
        return new Member(
                id, "유동현", "010-1111-1111", "my@gmail.com", ""
                , SchoolInformation.ofUnderGraduate("건축공학과", 3)
                , new IbasInformation(Role.BASIC_MEMBER));
    }


}
