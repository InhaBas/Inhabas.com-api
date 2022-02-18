package com.inhabas.api.domain.member.type.wrapper;

public enum Role {
    ADMIN, // 사이트 관리자
    EXECUTIVES, // 회장단
    PROFESSOR, // 교수
    BASIC_MEMBER, // 일반회원
    DEACTIVATED_MEMBER, // 비활동회원
    NOT_APPROVED_MEMBER, // 가입 후 아직 승인되지 않은 회원
    ANONYMOUS // 익명.
}

