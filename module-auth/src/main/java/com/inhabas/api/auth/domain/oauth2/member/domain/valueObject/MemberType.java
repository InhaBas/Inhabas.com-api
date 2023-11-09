package com.inhabas.api.auth.domain.oauth2.member.domain.valueObject;

public enum MemberType {

    UNDERGRADUATE("학부생"),
    BACHELOR("학사"),
    GRADUATED("대학원생"),
    PROFESSOR("교수"),
    OTHER("기타");

    private final String value;

    MemberType(String value) {
        this.value = value;
    }
}

