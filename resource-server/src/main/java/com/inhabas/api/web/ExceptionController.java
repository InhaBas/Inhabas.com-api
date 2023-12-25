package com.inhabas.api.web;

import com.inhabas.api.auth.domain.error.ErrorResponse;
import com.inhabas.api.auth.domain.error.businessException.InvalidInputException;
import com.inhabas.api.domain.signUpSchedule.SignUpNotAvailableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

import static com.inhabas.api.auth.domain.error.ErrorCode.INVALID_INPUT_VALUE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler
    protected ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException e) {
        log.warn("ConstraintViolation occurred");
        final ErrorResponse response = ErrorResponse.of(INVALID_INPUT_VALUE);
        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    @ExceptionHandler(InvalidInputException.class)
    protected ResponseEntity<ErrorResponse> handleInvalidInputException(InvalidInputException e) {
        log.warn("Invalid input value");
        final ErrorResponse response = ErrorResponse.of(INVALID_INPUT_VALUE);
        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    @ExceptionHandler(SignUpNotAvailableException.class)
    protected ResponseEntity<String> notAllowedSignUpException(final SignUpNotAvailableException e) {
        log.warn("no token in header: ", e);

        return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler
    protected ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        final ErrorResponse response = ErrorResponse.of(INVALID_INPUT_VALUE);
        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    //400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> processValidationError(MethodArgumentNotValidException exception) {
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