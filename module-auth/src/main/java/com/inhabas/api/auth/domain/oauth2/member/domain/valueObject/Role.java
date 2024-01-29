package com.inhabas.api.auth.domain.oauth2.member.domain.valueObject;

/**
 * 모든 회원은 교수와 학생으로 나뉨. 교수와 학생 간 권한차이는 없음. <br>
 * 수직적 권한 계층은 Role 에 의해서 결정됨.
 */
public enum Role {
  ADMIN, // 사이트 관리자
  CHIEF, // 회장
  VICE_CHIEF, // 회장
  EXECUTIVES, // 회장단
  SECRETARY, // 총무
  BASIC, // 활동 일반회원 (교수 포함)
  DEACTIVATED, // 비활동회원 (졸업생 포험)
  NOT_APPROVED, // 가입 후 아직 승인되지 않은 회원
  SIGNING_UP, // Oauth 인증 후 회원가입을 완료하기 전 회원
  ANONYMOUS; // 익명. 유일하게 회원가입을 시도할 수 있는 권한. 즉 상위의 권한으로 회원가입 시도 불가 ;
}
