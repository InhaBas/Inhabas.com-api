package com.inhabas.api.config;

import static com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.inhabas.api.auth.config.AuthBeansConfig;
import com.inhabas.api.auth.domain.oauth2.member.security.Hierarchical;
import com.inhabas.api.auth.domain.token.CustomRequestMatcher;
import com.inhabas.api.auth.domain.token.JwtAccessDeniedHandler;
import com.inhabas.api.auth.domain.token.jwtUtils.JwtAuthenticationProvider;
import com.inhabas.api.auth.domain.token.jwtUtils.JwtTokenUtil;
import com.inhabas.api.auth.domain.token.securityFilter.JwtAuthenticationEntryPoint;
import com.inhabas.api.auth.domain.token.securityFilter.JwtAuthenticationFilter;

/**
 * api 엔드포인트에 대한 여러 보안 설정을 담당함. 인증 관련 보안 설정은 {@link com.inhabas.api.auth.config.AuthSecurityConfig
 * AuthSecurityConfig} 참고
 */
public class WebSecurityConfig {

  private static final String[] AUTH_WHITELIST_SWAGGER = {
    "/swagger-ui/**", "/swagger/**", "/docs/**", "/v3/api-docs/**", "/swagger-resources/**"
  };
  private static final String[] AUTH_WHITELIST_STATIC = {
    "/static/css/**", "/static/js/**", "/**/*.ico", "/favicon.ico"
  };
  private static final String[] AUTH_WHITELIST_TOKEN = {"/token/**"};
  private static final String[] AUTH_WHITELIST_PATH = {
    "/menu/**", "/menus", "/member/chief", "/error"
  };
  private static final String[] AUTH_WHITELIST_SIGNUP = {
    "/signUp/schedule", "/signUp/questionnaires", "/signUp/majorInfo"
  };
  private static final String[] AUTH_WHITELIST_CLUB = {"/club/histories", "/club/history/**"};
  private static final String[] AUTH_WHITELIST_POLICY = {"/policy/**"};
  private static final String[] AUTH_WHITELIST_CLUB_ACTIVITY = {
    "/club/activity/**", "/club/activities"
  };
  private static final String[] AUTH_WHITELIST_NORMAL_BOARD = {"/board/count"};
  private static final String[] AUTH_WHITELIST_PROJECT_BOARD = {"/project/count"};
  private static final String[] AUTH_WHITELIST_CONTEST_BOARD = {"/contest/count"};

  @Configuration
  @EnableMethodSecurity(jsr250Enabled = true)
  @EnableWebSecurity
  @RequiredArgsConstructor
  @Profile({"local", "dev1", "dev2", "default_mvc_test", "prod1", "prod2", "integration_test"})
  public static class ApiSecurityForDev {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final Hierarchical hierarchy;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthBeansConfig authBeansConfig;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    @Value("${cors.allowed-origins:*}")
    private String allowedOriginsProp;

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
      return new ProviderManager(List.of(jwtAuthenticationProvider));
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
      CorsConfiguration config = new CorsConfiguration();

      List<String> origins =
          Arrays.stream(allowedOriginsProp.split(","))
              .map(String::trim)
              .filter(s -> !s.isEmpty())
              .collect(Collectors.toList());

      boolean wildcard = origins.contains("*");
      if (wildcard) {
        // 패턴 전체 허용 시, 자격 증명은 허용하지 않음 (보안 권장)
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowCredentials(false);
      } else {
        config.setAllowedOrigins(origins);
        config.setAllowCredentials(true);
      }

      config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
      config.setAllowedHeaders(List.of("*"));
      config.setExposedHeaders(List.of("Authorization", "Content-Disposition"));
      config.setMaxAge(3600L);

      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      source.registerCorsConfiguration("/**", config);
      return source;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
      return (web) -> {
        // 동적 API는 필터 체인을 타게 두고, 정적 리소스만 완전히 무시
        for (String pattern : AUTH_WHITELIST_STATIC) {
          web.ignoring().requestMatchers(new AntPathRequestMatcher(pattern));
        }
      };
    }

    @Bean
    @Order(1)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      http.anonymous(
              anonymous -> anonymous.principal("anonymousUser").authorities("ROLE_" + ANONYMOUS))
          .httpBasic(httpBasic -> httpBasic.disable())
          .sessionManagement(
              sessionManagement ->
                  sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
          .cors(cors -> cors.configurationSource(corsConfigurationSource()))
          .csrf(AbstractHttpConfigurer::disable)
          .headers(
              headers ->
                  headers
                      .referrerPolicy(
                          r -> r.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.NO_REFERRER))
                      .frameOptions(frame -> frame.sameOrigin()))
          .exceptionHandling(
              exceptionHandling ->
                  exceptionHandling
                      .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                      .accessDeniedHandler(jwtAccessDeniedHandler))
          .authorizeRequests(
              authorize ->
                  authorize
                      .expressionHandler(expressionHandler())
                      .requestMatchers(CorsUtils::isPreFlightRequest)
                      .permitAll()
                      // Swagger 및 공개 경로는 필터 체인은 타되 인가만 면제
                      .requestMatchers(pathMatchers(AUTH_WHITELIST_SWAGGER))
                      .permitAll()
                      .requestMatchers(pathMatchers(AUTH_WHITELIST_PATH))
                      .permitAll()
                      // GET 공개 엔드포인트들
                      .requestMatchers(methodMatchers(HttpMethod.GET, AUTH_WHITELIST_POLICY))
                      .permitAll()
                      .requestMatchers(methodMatchers(HttpMethod.GET, AUTH_WHITELIST_SIGNUP))
                      .permitAll()
                      .requestMatchers(methodMatchers(HttpMethod.GET, AUTH_WHITELIST_CLUB))
                      .permitAll()
                      .requestMatchers(methodMatchers(HttpMethod.GET, AUTH_WHITELIST_NORMAL_BOARD))
                      .permitAll()
                      .requestMatchers(methodMatchers(HttpMethod.GET, AUTH_WHITELIST_PROJECT_BOARD))
                      .permitAll()
                      .requestMatchers(methodMatchers(HttpMethod.GET, AUTH_WHITELIST_CONTEST_BOARD))
                      .permitAll()
                      // 나머지 기존 인가 규칙 유지
                      .requestMatchers(pathMatchers("/myInfo/requests"))
                      .hasAnyRole(CHIEF.toString(), VICE_CHIEF.toString())
                      .requestMatchers(pathMatchers("/myInfo/request/**"))
                      .hasAnyRole(CHIEF.toString(), VICE_CHIEF.toString())
                      .requestMatchers(pathMatchers("/members/executive"))
                      .hasRole(ANONYMOUS.toString())
                      .requestMatchers(pathMatchers("/members/hof"))
                      .hasRole(DEACTIVATED.toString())
                      .requestMatchers(pathMatchers("/members/approved/role"))
                      .hasRole(SECRETARY.toString())
                      .requestMatchers(pathMatchers("/members/approved/type"))
                      .hasAnyRole(CHIEF.toString(), VICE_CHIEF.toString())
                      .requestMatchers(pathMatchers("/members/**", "/member/**"))
                      .hasAnyRole(SECRETARY.toString(), EXECUTIVES.toString())
                      .requestMatchers(
                          pathMatchers(
                              "/budget/history/**",
                              "/budget/histories",
                              "/budget/application/**",
                              "/budget/applications"))
                      .hasRole(DEACTIVATED.toString())
                      .requestMatchers(pathMatchers("/lecture/**/status"))
                      .hasRole(EXECUTIVES.toString())
                      .requestMatchers(pathMatchers("/lecture/**"))
                      .hasRole(DEACTIVATED.toString())
                      .requestMatchers(methodMatchers(HttpMethod.PUT, "/signUp/schedule"))
                      .hasAnyRole(CHIEF.toString(), VICE_CHIEF.toString())
                      .requestMatchers(pathMatchers("/signUp/check"))
                      .hasRole(ANONYMOUS.toString())
                      .requestMatchers(pathMatchers("/signUp/**"))
                      .hasRole(SIGNING_UP.toString())
                      .requestMatchers(pathMatchers("/club/history/**"))
                      .hasRole(EXECUTIVES.toString())
                      .requestMatchers(methodMatchers(HttpMethod.PUT, "/policy/**"))
                      .hasAnyRole(CHIEF.toString(), VICE_CHIEF.toString())
                      .requestMatchers(pathMatchers("/scholarship/history/**"))
                      .hasRole(SECRETARY.toString())
                      .anyRequest()
                      .hasRole(ANONYMOUS.toString()));

      http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

      return http.build();
    }

    private static RequestMatcher[] pathMatchers(String... patterns) {
      return Arrays.stream(patterns).map(AntPathRequestMatcher::new).toArray(RequestMatcher[]::new);
    }

    private static RequestMatcher[] methodMatchers(HttpMethod method, String... patterns) {
      return Arrays.stream(patterns)
          .map(p -> new AntPathRequestMatcher(p, method.name()))
          .toArray(RequestMatcher[]::new);
    }

    @Bean
    public SecurityExpressionHandler<FilterInvocation> expressionHandler() {
      DefaultWebSecurityExpressionHandler webSecurityExpressionHandler =
          new DefaultWebSecurityExpressionHandler();
      webSecurityExpressionHandler.setRoleHierarchy(hierarchy.getHierarchy());
      return webSecurityExpressionHandler;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
      final List<RequestMatcher> skipPaths = new ArrayList<>();

      for (String path : AUTH_WHITELIST_CLUB_ACTIVITY) {
        skipPaths.add(new CustomRequestMatcher(path, "GET"));
      }

      for (String path : AUTH_WHITELIST_TOKEN) {
        skipPaths.add(new CustomRequestMatcher(path, "POST"));
      }

      final RequestMatcher requestMatcher = new AndRequestMatcher(skipPaths);
      final JwtAuthenticationFilter filter =
          new JwtAuthenticationFilter(
              requestMatcher, jwtTokenUtil, authBeansConfig.tokenResolver());
      filter.setAuthenticationManager(authenticationManager());
      return filter;
    }
  }
}
