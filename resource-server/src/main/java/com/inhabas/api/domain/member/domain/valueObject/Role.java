package com.inhabas.api.domain.member.domain.valueObject;

/**
 * 모든 회원은 교수와 학생으로 나뉨. 교수와 학생 간 권한차이는 없음. <br>수직적 권한 계층은 Role 에 의해서 결정됨.
 */
public enum Role {
    ADMIN(1), // 사이트 관리자
    CHIEF(2), // 회장
    EXECUTIVES(3), // 회장단
    SECRETARY(4), // 총무
    BASIC(5), // 활동 일반회원 (교수 포함)
    DEACTIVATED(6), // 비활동회원 (졸업생 포험)
    NOT_APPROVED(7), // 가입 후 아직 승인되지 않은 회원
    ANONYMOUS(8); // 익명. 유일하게 회원가입을 시도할 수 있는 권한. 즉 상위의 권한으로 회원가입 시도 불가 ;

    private final int number;

    Role(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

}

