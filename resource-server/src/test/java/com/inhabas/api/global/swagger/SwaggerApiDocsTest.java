package com.inhabas.api.global.swagger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inhabas.api.ApiApplication;
import com.inhabas.testAnnotataion.CustomSpringBootTest;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@CustomSpringBootTest(classes = ApiApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SwaggerApiDocsTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  private JsonNode apiDocs;

  @BeforeAll
  public void loadApiDocs() throws Exception {
    String docs =
        mockMvc
            .perform(get("/v3/api-docs/All"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);
    apiDocs = objectMapper.readTree(docs);
  }

  @Test
  @DisplayName("커스텀 응답 어노테이션의 에러 응답이 문서에 포함된다.")
  public void customAnnotationErrorResponsesAreDocumented() {
    JsonNode responses =
        apiDocs.path("paths").path("/board/{boardType}/{boardId}").path("get").path("responses");

    assertThat(responses.has("200")).isTrue();
    assertThat(responses.has("400")).isTrue();
    assertThat(responses.has("404")).isTrue();
    assertThat(responses.path("400").path("description").asText())
        .isEqualTo(SwaggerErrorExamples.INVALID_INPUT_DESC);
    assertThat(responses.path("404").path("description").asText())
        .isEqualTo(SwaggerErrorExamples.NOT_FOUND_DESC);
  }

  @Test
  @DisplayName("201 생성 응답과 에러 응답이 함께 문서화된다.")
  public void createdResponseIsDocumented() {
    JsonNode responses =
        apiDocs.path("paths").path("/board/{boardType}").path("post").path("responses");

    assertThat(responses.has("201")).isTrue();
    assertThat(responses.has("400")).isTrue();
    assertThat(responses.has("404")).isTrue();
    assertThat(responses.path("201").path("description").asText())
        .isEqualTo(SwaggerErrorExamples.CREATED_DESC);
  }

  @Test
  @DisplayName("회원가입 API 에 403 에러 응답이 문서화된다.")
  public void signUpForbiddenResponseIsDocumented() {
    JsonNode responses = apiDocs.path("paths").path("/signUp").path("get").path("responses");

    assertThat(responses.has("200")).isTrue();
    assertThat(responses.has("403")).isTrue();
    assertThat(responses.path("403").path("description").asText())
        .isEqualTo(SwaggerErrorExamples.SIGNUP_NOT_AVAILABLE_DESC);
  }

  @Test
  @DisplayName("응답 코드에 공백이 포함된 잘못된 키가 문서에 존재하지 않는다.")
  public void noResponseCodesWithTrailingWhitespace() {
    apiDocs
        .path("paths")
        .forEach(
            path ->
                path.forEach(
                    operation ->
                        operation
                            .path("responses")
                            .fieldNames()
                            .forEachRemaining(code -> assertThat(code).isEqualTo(code.strip()))));
  }
}
