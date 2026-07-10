package com.inhabas.testSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/** 컨트롤러 슬라이스 테스트의 공통 의존성(MockMvc, ObjectMapper)과 직렬화 헬퍼를 제공한다. */
public abstract class ControllerTestSupport {

  @Autowired protected MockMvc mvc;

  @Autowired protected ObjectMapper objectMapper;

  protected String jsonOf(Object response) throws JsonProcessingException {
    return objectMapper.writeValueAsString(response);
  }
}
