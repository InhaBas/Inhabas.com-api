package com.inhabas.api.auth.domain.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

  // Global
  INTERNAL_SERVER_ERROR(500, "G001", "내부 서버 오류입니다."),
  METHOD_NOT_ALLOWED(405, "G002", "허용되지 않은 HTTP method입니다."),
  INVALID_INPUT_VALUE(400, "G003", "입력값이 없거나, 타입이 유효하지 않습니다."),
  NOT_FOUND(404, "G004", "데이터가 존재하지 않습니다."),
  UNSUPPORTED_MEDIA_TYPE(415, "G005", "지원되지 않는 형식입니다."),

  // Basic

  // Member
  MEMBER_NOT_FOUND(400, "M001", "존재 하지 않는 유저입니다."),
  AUTHORITY_INVALID(403, "M002", "권한이 없습니다."),

  // Auth (ExceptionHandler 불필요)
  /** The value of one or more redirection URIs is unauthorized. */
  UNAUTHORIZED_REDIRECT_URI(401, "A001", "유효하지 않은 redirect_uri 입니다."),
  /** 지원하지 않는 소셜로그인 */
  UNSUPPORTED_OAUTH2_PROVIDER(401, "A002", "지원하지 않는 소셜로그인입니다."),
  /** 인증에 필수적인 정보가 OAuth provider 로부터 전달되지 않았음. 사용자가 개인정보 제공에 비동의했거나, 제대로 계정 정보를 설정하지 않은 경우 발생 */
  INVALID_OAUTH2_INFO(401, "A003", "잘못된 OAuth 정보입니다."),
  /**
   * request 에 담긴 토큰정보를 사용해 기존 사용자 정보를 조회하였으나, 존재하지 않는 경우 발생. 또는 최초 소셜로그인 시도하였으나 가입한 회원이 아니라면 해당 오류
   * 발생
   *
   * @see com.inhabas.api.auth.domain.token.securityFilter;
   */
  SOCIAL_NOT_FOUND(401, "A004", "기존 회원이 아니거나 회원가입 되지 않은 회원입니다."),
  JWT_INVALID(401, "A005", "유효하지 않은 토큰입니다."),
  JWT_EXPIRED(401, "A006", "만료된 토큰입니다."),
  JWT_MISSING(401, "A007", "토큰이 존재하지 않습니다."),
  EXPIRED_REFRESH_TOKEN(401, "A008", "만료된 REFRESH 토큰입니다. 재로그인 해주십시오."),

  // SignUp
  SIGNUP_NOT_AVAILABLE(403, "S001", "회원가입 기간이 아닙니다."),
  INVALID_SIGNUP_DATE(400, "S002", "등록 마감일이 시작일보다 앞설 수 없습니다."),
  INVALID_INTERVIEW_DATE(400, "S003", "면접 마감일이 시작일보다 앞설 수 없습니다."),
  INVALID_ANNOUNCE_DATE(400, "S004", "결과 발표일이 면접 마감일보다 앞설 수 없습니다."),
  NOT_WRITE_PROFILE(400, "S005", "아직 회원 프로필을 생성하지 않아서 회원가입을 마무리 할 수 없습니다."),
  NOT_WRITE_ANSWERS(400, "S006", "아직 면접 질문을 작성하지 않아서 회원가입을 마무리 할 수 없습니다."),

  // Board
  WRITER_UNMODIFIABLE(403, "B001", "글 작성자를 변경 할 수 없습니다."),
  ONLY_WRITER_UPDATE(403, "B002", "글 작성자만 수정 가능합니다."),
  S3_UPLOAD_FAILED(500, "B003", "파일 업로드를 실패했습니다.");

  private final int status;
  private final String code;
  private final String message;
}
