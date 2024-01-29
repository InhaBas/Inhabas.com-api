package com.inhabas.api.web;

import static com.inhabas.api.auth.domain.error.ErrorCode.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;

import javax.validation.ConstraintViolationException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.inhabas.api.auth.domain.error.ErrorResponse;
import com.inhabas.api.auth.domain.error.businessException.InvalidInputException;
import com.inhabas.api.auth.domain.error.businessException.NotFoundException;
import com.inhabas.api.domain.signUp.exception.NotWriteAnswersException;
import com.inhabas.api.domain.signUp.exception.NotWriteProfileException;
import com.inhabas.api.domain.signUpSchedule.exception.InvalidDateException;
import com.inhabas.api.domain.signUpSchedule.exception.SignUpNotAvailableException;

@Slf4j
@RestControllerAdvice
public class ExceptionController {

  @ExceptionHandler
  protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
      MethodArgumentTypeMismatchException e) {
    log.error("Invalid method argument type", e);
    final ErrorResponse response = ErrorResponse.of(INVALID_INPUT_VALUE);
    return new ResponseEntity<>(response, BAD_REQUEST);
  }

  @ExceptionHandler
  @ResponseStatus
  public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(NoHandlerFoundException e) {
    log.error("Not found", e);
    final ErrorResponse response = ErrorResponse.of(NOT_FOUND);
    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(NotFoundException e) {
    log.error("Not found", e);
    final ErrorResponse response = ErrorResponse.of(NOT_FOUND);
    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler
  protected ResponseEntity<ErrorResponse> handleConstraintViolationException(
      ConstraintViolationException e) {
    log.error("Database ConstraintViolation occurred", e);
    final ErrorResponse response = ErrorResponse.of(INVALID_INPUT_VALUE);
    return new ResponseEntity<>(response, BAD_REQUEST);
  }

  @ExceptionHandler(InvalidInputException.class)
  protected ResponseEntity<ErrorResponse> handleInvalidInputException(InvalidInputException e) {
    log.error("Invalid input value", e);
    final ErrorResponse response = ErrorResponse.of(INVALID_INPUT_VALUE);
    return new ResponseEntity<>(response, BAD_REQUEST);
  }

  @ExceptionHandler(InvalidDateException.class)
  protected ResponseEntity<ErrorResponse> handleInvalidDateException(InvalidDateException e) {
    log.error("Invalid SignUp date", e);
    final ErrorResponse response = ErrorResponse.of(e.getErrorCode());
    return new ResponseEntity<>(response, BAD_REQUEST);
  }

  @ExceptionHandler(SignUpNotAvailableException.class)
  protected ResponseEntity<ErrorResponse> handleNotAllowedSignUpException(
      SignUpNotAvailableException e) {
    log.error("Not registration period now", e);
    final ErrorResponse response = ErrorResponse.of(SIGNUP_NOT_AVAILABLE);
    return new ResponseEntity<>(response, FORBIDDEN);
  }

  @ExceptionHandler(NotWriteProfileException.class)
  protected ResponseEntity<ErrorResponse> handleNotWriteProfileException(
      NotWriteProfileException e) {
    log.error("Must write profile before signup", e);
    final ErrorResponse response = ErrorResponse.of(NOT_WRITE_PROFILE);
    return new ResponseEntity<>(response, BAD_REQUEST);
  }

  @ExceptionHandler(NotWriteAnswersException.class)
  protected ResponseEntity<ErrorResponse> handleNotWriteAnswersException(
      NotWriteAnswersException e) {
    log.error("Must write answers before signup", e);
    final ErrorResponse response = ErrorResponse.of(NOT_WRITE_ANSWERS);
    return new ResponseEntity<>(response, BAD_REQUEST);
  }

  @ExceptionHandler({HttpMessageNotReadableException.class})
  protected ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
      HttpMessageNotReadableException e) {
    log.error("The request does not have the proper DTO format.", e);
    final ErrorResponse response = ErrorResponse.of(INVALID_INPUT_VALUE);
    return new ResponseEntity<>(response, BAD_REQUEST);
  }

  // 400
  @ExceptionHandler(MethodArgumentNotValidException.class)
  protected ResponseEntity<Object> processValidationError(MethodArgumentNotValidException e) {
    log.error("Validation test failed", e);
    final ErrorResponse response = ErrorResponse.of(INVALID_INPUT_VALUE);
    return new ResponseEntity<>(response, BAD_REQUEST);
  }
}
