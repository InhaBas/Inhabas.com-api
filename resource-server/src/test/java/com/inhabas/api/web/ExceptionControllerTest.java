package com.inhabas.api.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Set;

import jakarta.validation.ConstraintViolationException;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.inhabas.api.auth.domain.error.businessException.InvalidInputException;
import com.inhabas.api.auth.domain.error.businessException.NotFoundException;
import com.inhabas.api.domain.signUpSchedule.exception.SignUpNotAvailableException;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ExceptionControllerTest {

  private MockMvc mvc;

  @BeforeEach
  void setUp() {
    mvc =
        MockMvcBuilders.standaloneSetup(new StubController())
            .setControllerAdvice(new ExceptionController())
            .build();
  }

  @RestController
  static class StubController {

    @GetMapping("/test/not-found")
    public void notFound() {
      throw new NotFoundException();
    }

    @GetMapping("/test/invalid-input")
    public void invalidInput() {
      throw new InvalidInputException();
    }

    @GetMapping("/test/constraint-violation")
    public void constraintViolation() {
      throw new ConstraintViolationException("invalid", Set.of());
    }

    @GetMapping("/test/signup-not-available")
    public void signUpNotAvailable() {
      throw new SignUpNotAvailableException();
    }

    @GetMapping("/test/size-limit")
    public void sizeLimit() throws SizeLimitExceededException {
      throw new SizeLimitExceededException("file too large", 100L, 10L);
    }

    @GetMapping("/test/type-mismatch/{id}")
    public void typeMismatch(@PathVariable Integer id) {}

    @PostMapping("/test/body")
    public void body(@RequestBody BodyDto dto) {}
  }

  static class BodyDto {
    public String name;
  }

  @DisplayName("NotFoundException은 404와 G004 코드로 응답한다.")
  @Test
  void handleNotFoundException() throws Exception {
    mvc.perform(get("/test/not-found"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.code").value("G004"));
  }

  @DisplayName("InvalidInputException은 400과 G003 코드로 응답한다.")
  @Test
  void handleInvalidInputException() throws Exception {
    mvc.perform(get("/test/invalid-input"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("G003"));
  }

  @DisplayName("ConstraintViolationException은 400과 G003 코드로 응답한다.")
  @Test
  void handleConstraintViolationException() throws Exception {
    mvc.perform(get("/test/constraint-violation"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("G003"));
  }

  @DisplayName("SignUpNotAvailableException은 403으로 응답한다.")
  @Test
  void handleSignUpNotAvailableException() throws Exception {
    mvc.perform(get("/test/signup-not-available"))
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.status").value(403));
  }

  @DisplayName("SizeLimitExceededException은 413과 F003 코드로 응답한다.")
  @Test
  void handleSizeLimitExceededException() throws Exception {
    mvc.perform(get("/test/size-limit"))
        .andExpect(status().isPayloadTooLarge())
        .andExpect(jsonPath("$.code").value("F003"));
  }

  @DisplayName("파라미터 타입이 일치하지 않으면 400과 G003 코드로 응답한다.")
  @Test
  void handleMethodArgumentTypeMismatchException() throws Exception {
    mvc.perform(get("/test/type-mismatch/{id}", "not-a-number"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("G003"));
  }

  @DisplayName("본문 형식이 유효하지 않으면 400과 G003 코드로 응답한다.")
  @Test
  void handleHttpMessageNotReadableException() throws Exception {
    mvc.perform(post("/test/body").contentType(MediaType.APPLICATION_JSON).content("{invalid-json"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("G003"));
  }
}
