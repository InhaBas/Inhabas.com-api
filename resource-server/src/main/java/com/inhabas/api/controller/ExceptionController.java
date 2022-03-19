package com.inhabas.api.controller;

import com.inhabas.api.domain.signup.SignUpNotAvailableException;
import com.inhabas.api.security.domain.NoTokenInRequestHeaderException;
import com.inhabas.api.security.domain.RefreshTokenNotFoundException;
import com.inhabas.api.security.jwtUtils.InvalidJwtTokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(SignUpNotAvailableException.class)
    public ResponseEntity<String> notAllowedSignUpException(final SignUpNotAvailableException e) {
        log.warn("no token in header: ", e);

        return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NoTokenInRequestHeaderException.class)
    public ResponseEntity<String> corruptedTokenException(final NoTokenInRequestHeaderException e) {
        log.warn("no token in header: ", e);

        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(RefreshTokenNotFoundException.class)
    public ResponseEntity<String> corruptedTokenException(final RefreshTokenNotFoundException e) {
        log.warn("invalid refresh token: ", e);

        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> notAllowedException(final AccessDeniedException e) {
        log.warn("access denied: ", e);

        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    //400
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> BadRequestException(final RuntimeException exception) {
        log.warn("invalid request: ", exception);

        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    //400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> processValidationError(MethodArgumentNotValidException exception) {
        log.warn("invalid request: ", exception);

        BindingResult bindingResult = exception.getBindingResult();

        StringBuilder builder = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            builder.append("[");
            builder.append(fieldError.getField());
            builder.append("](은)는 ");
            builder.append(fieldError.getDefaultMessage());
            builder.append(" 입력된 값: [");
            builder.append(fieldError.getRejectedValue());
            builder.append("]\n");
        }

        return ResponseEntity.badRequest().body(builder.toString());
    }
}
