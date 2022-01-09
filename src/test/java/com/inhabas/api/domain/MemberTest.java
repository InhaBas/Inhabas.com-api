package com.inhabas.api.domain;

import com.inhabas.api.domain.member.*;
import com.inhabas.api.domain.member.type.wrapper.*;

public class MemberTest {
    public static final Member MEMBER1 = new Member(
            12171234, "유동현", "010-1111-1111", ""
            , new SchoolInformation(Major.건축공학과, 3, 1)
            , new IbasInformation(Role.normalMember, "hello", 0));

    public static final Member MEMBER2 = new Member(
            12114321, "김민겸", "010-2222-2222", ""
            , new SchoolInformation(Major.경영학과, 2, 2)
            , new IbasInformation(Role.normalMember, "hi", 0));




}
