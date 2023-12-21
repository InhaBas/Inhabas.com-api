package com.inhabas.api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.apache.http.HttpHeaders;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class SwaggerConfig {

    private final static String DOMAIN = "https://dev.inhabas.com/api";

    private Info apiInfo() {
        return new Info()
                .version("v1.0.0")
                .title("Inhabas API")
                .description("spring-api docs");
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

    private OpenAPI createOpenAPI(Info info, SecurityScheme securityScheme, SecurityRequirement securityRequirement, Server... servers) {
        OpenAPI openAPI = new OpenAPI()
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

        return GroupedOpenApi
                .builder()
                .group("All")
                .pathsToMatch("/**")
                .build();

    }

    @Bean
    public GroupedOpenApi getBasicApi() {

        return GroupedOpenApi
                .builder()
                .group("기본")
                .pathsToMatch("/policy/**", "/menu/**", "/menus/**", "/member/chief")
                .build();

    }

    @Bean
    public GroupedOpenApi getMemberApi() {

        return GroupedOpenApi
                .builder()
                .group("회원 관련")
                .pathsToMatch("/signUp/**", "/members/**", "/member/**", "/myInfo/**")
                .pathsToExclude("/member/chief")
                .build();

    }

    @Bean
    @Profile("local")
    public OpenAPI localOpenAPI() {
        return createOpenAPI(apiInfo(), securityScheme(), securityRequirement());
    }
}