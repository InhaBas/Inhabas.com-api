package com.inhabas.api.auth.domain.token;

import static com.inhabas.api.auth.domain.error.ErrorCode.AUTHORITY_INVALID;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inhabas.api.auth.domain.error.ErrorResponse;

@Component
@Slf4j
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

  @Override
  public void handle(
      HttpServletRequest request,
      HttpServletResponse response,
      AccessDeniedException accessDeniedException)
      throws IOException, ServletException {
    log.error(accessDeniedException.getLocalizedMessage());
    response.setStatus(AUTHORITY_INVALID.getStatus());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    try (OutputStream os = response.getOutputStream()) {
      ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.writeValue(os, ErrorResponse.of(AUTHORITY_INVALID));
      os.flush();
    }
  }
}
