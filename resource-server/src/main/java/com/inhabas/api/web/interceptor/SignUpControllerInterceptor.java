package com.inhabas.api.web.interceptor;

import com.inhabas.api.domain.signUpSchedule.exception.SignUpNotAvailableException;
import com.inhabas.api.domain.signUpSchedule.usecase.SignUpAvailabilityChecker;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;

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
