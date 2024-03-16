package com.inhabas.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.apache.http.HttpHeaders;
import org.springdoc.core.GroupedOpenApi;

@Configuration
public class SwaggerConfig {

  private static final String DOMAIN = "https://dev.inhabas.com/api";

  private Info apiInfo() {
    return new Info().version("v1.0.0").title("Inhabas API").description("spring-api docs");
  }

  private SecurityScheme securityScheme() {
    return new SecurityScheme()
        .type(SecurityScheme.Type.HTTP)
        .scheme("bearer")
        .bearerFormat("JWT")
        .in(SecurityScheme.In.HEADER)
        .name(HttpHeaders.AUTHORIZATION);
  }

  private SecurityRequirement securityRequirement() {
    SecurityRequirement requirement = new SecurityRequirement();
    requirement.addList("JWT");
    return requirement;
  }

  private OpenAPI createOpenAPI(
      Info info,
      SecurityScheme securityScheme,
      SecurityRequirement securityRequirement,
      Server... servers) {
    OpenAPI openAPI =
        new OpenAPI()
            .components(new Components().addSecuritySchemes("JWT", securityScheme))
            .addSecurityItem(securityRequirement)
            .info(info);

    for (Server server : servers) {
      openAPI.addServersItem(server);
    }

    return openAPI;
  }

  @Bean
  @Profile("dev")
  public OpenAPI devOpenAPI() {
    // HTTPS 서버 URL 설정
    Server server = new Server().url(DOMAIN);

    return createOpenAPI(apiInfo(), securityScheme(), securityRequirement(), server);
  }

  @Bean
  public GroupedOpenApi getAllApi() {

    return GroupedOpenApi.builder().group("All").pathsToMatch("/**").build();
  }

  @Bean
  public GroupedOpenApi getBasicApi() {

    return GroupedOpenApi.builder()
        .group("기본")
        .pathsToMatch("/policy/**", "/menu/**", "/menus/**", "/member/chief")
        .build();
  }

  @Bean
  public GroupedOpenApi getMemberApi() {

    return GroupedOpenApi.builder()
        .group("회원 관련")
        .pathsToMatch("/signUp/**", "/members/**", "/member/**", "/myInfo/**")
        .pathsToExclude("/member/chief")
        .build();
  }

  @Bean
  public GroupedOpenApi getIBASApi() {

    return GroupedOpenApi.builder()
        .group("IBAS 관련")
        .pathsToMatch("/club/**", "/**/**/**/comment/**", "/**/**/**/comments")
        .build();
  }

  @Bean
  public GroupedOpenApi getBoardApi() {

    return GroupedOpenApi.builder()
        .group("게시판 관련")
        .pathsToMatch("/board/**", "/**/**/**/comment/**", "/**/**/**/comments")
        .build();
  }

  @Bean
  public GroupedOpenApi getBudgetApi() {

    return GroupedOpenApi.builder().group("회계 관련").pathsToMatch("/budget/**").build();
  }

  @Bean
  public GroupedOpenApi getProjectBoardApi() {

    return GroupedOpenApi.builder()
        .group("프로젝트 게시판 관련")
        .pathsToMatch("/project/**", "/**/**/**/comment/**", "/**/**/**/comments")
        .build();
  }

  @Bean
  public GroupedOpenApi getContestApi() {

    return GroupedOpenApi.builder()
        .group("공모전 게시판 관련")
        .pathsToMatch("/contest/**", "/**/**/**/comment/**", "/**/**/**/comments")
        .build();
  }

  @Bean
  @Profile("local")
  public OpenAPI localOpenAPI() {
    return createOpenAPI(apiInfo(), securityScheme(), securityRequirement());
  }
}
