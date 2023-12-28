package com.inhabas.api.web.interceptor;

import com.inhabas.api.domain.signUpSchedule.usecase.SignUpAvailabilityChecker;
import com.inhabas.api.domain.signUpSchedule.domain.exception.SignUpNotAvailableException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RequiredArgsConstructor
public class SignUpControllerInterceptor implements HandlerInterceptor {

    private final SignUpAvailabilityChecker availabilityChecker;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (availabilityChecker.isAvailable()) {
            return true;
        }
        else
            throw new SignUpNotAvailableException();
    }
}
