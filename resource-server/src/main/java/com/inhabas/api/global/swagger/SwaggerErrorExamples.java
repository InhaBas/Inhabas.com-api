package com.inhabas.api.global.swagger;

/** 공통 API 응답 문서화에 사용하는 설명 및 에러 예시 상수. */
public final class SwaggerErrorExamples {

  public static final String CREATED_DESC = "'Location' 헤더에 생성된 리소스의 URI 가 포함됩니다.";

  public static final String INVALID_INPUT_DESC = "입력값이 없거나, 타입이 유효하지 않습니다.";
  public static final String INVALID_INPUT_EXAMPLE =
      "{\"status\": 400, \"code\": \"G003\", \"message\": \"입력값이 없거나, 타입이 유효하지 않습니다.\"}";

  public static final String NOT_FOUND_DESC = "데이터가 존재하지 않습니다.";
  public static final String NOT_FOUND_EXAMPLE =
      "{\"status\": 404, \"code\": \"G004\", \"message\": \"데이터가 존재하지 않습니다.\"}";

  public static final String SIGNUP_NOT_AVAILABLE_DESC = "회원가입 기간이 아닙니다.";
  public static final String SIGNUP_NOT_AVAILABLE_EXAMPLE =
      "{\"status\": 403, \"code\": \"S001\", \"message\": \"회원가입 기간이 아닙니다.\"}";

  private SwaggerErrorExamples() {}
}
