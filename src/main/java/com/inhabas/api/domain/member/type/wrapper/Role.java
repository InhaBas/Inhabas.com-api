package com.inhabas.api.domain.member.type.wrapper;

/**
 * 모든 회원은 교수와 학생으로 나뉨. 교수와 학생 간 권한차이는 없음. <br>수직적 권한 계층은 Role 에 의해서 결정됨.
 */
public enum Role {
    ADMIN, // 사이트 관리자
    EXECUTIVES, // 회장단
    BASIC_MEMBER, // 일반회원
    DEACTIVATED_MEMBER, // 비활동회원
    NOT_APPROVED_MEMBER, // 가입 후 아직 승인되지 않은 회원
    ANONYMOUS // 익명.
}

