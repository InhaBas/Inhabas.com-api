package com.inhabas.api.global.swagger;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.inhabas.api.auth.domain.error.ErrorResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/** 204 성공 응답과 400(G003 입력값 오류) + 403(S001 회원가입 기간 아님) 에러 응답을 문서화한다. */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses({
  @ApiResponse(responseCode = "204"),
  @ApiResponse(
      responseCode = "400",
      description = SwaggerErrorExamples.INVALID_INPUT_DESC,
      content =
          @Content(
              schema = @Schema(implementation = ErrorResponse.class),
              examples = @ExampleObject(value = SwaggerErrorExamples.INVALID_INPUT_EXAMPLE))),
  @ApiResponse(
      responseCode = "403",
      description = SwaggerErrorExamples.SIGNUP_NOT_AVAILABLE_DESC,
      content =
          @Content(
              schema = @Schema(implementation = ErrorResponse.class),
              examples = @ExampleObject(value = SwaggerErrorExamples.SIGNUP_NOT_AVAILABLE_EXAMPLE)))
})
public @interface Response204And400AndSignUpNotAvailable {}
