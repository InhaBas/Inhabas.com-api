package com.inhabas.api.web.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.web.servlet.HandlerInterceptor;

import com.inhabas.api.domain.signUpSchedule.exception.SignUpNotAvailableException;
import com.inhabas.api.domain.signUpSchedule.usecase.SignUpAvailabilityChecker;

@RequiredArgsConstructor
public class SignUpControllerInterceptor implements HandlerInterceptor {

  private final SignUpAvailabilityChecker availabilityChecker;

  @Override
  public boolean preHandle(
      HttpServletRequest request, HttpServletResponse response, Object handler) {
    if (availabilityChecker.isAvailable()) {
      return true;
    } else throw new SignUpNotAvailableException();
  }
}
