package com.inhabas.api.config;

import static com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role.*;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsUtils;

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
    "/swagger-ui/**", "/swagger/**", "/docs/**"
  };
  private static final String[] AUTH_WHITELIST_STATIC = {
    "/static/css/**", "/static/js/**", "*.ico"
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
  @Order(1)
  @EnableMethodSecurity(jsr250Enabled = true)
  @EnableWebSecurity
  @RequiredArgsConstructor
  @Profile({"local", "dev1", "dev2", "default_mvc_test", "prod1", "prod2", "integration_test"})
  public static class ApiSecurityForDev {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final Hierarchical hierarchy;
    private final JwtTokenUtil jwtTokenUtil;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final AuthBeansConfig authBeansConfig;

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
      AuthenticationManagerBuilder builder =
          http.getSharedObject(AuthenticationManagerBuilder.class);
      builder.authenticationProvider(jwtAuthenticationProvider);
      return builder.build();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(
        AuthenticationManager authenticationManager) {
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

      filter.setAuthenticationManager(authenticationManager);
      return filter;
    }

    @Bean
    @Order(1)
    public SecurityFilterChain apiSecurityFilterChain(
        HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
      http.securityMatcher("/**")
          .anonymous(anon -> anon.authorities("ROLE_" + ANONYMOUS))
          .httpBasic(AbstractHttpConfigurer::disable)
          .sessionManagement(
              session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
          .cors(Customizer.withDefaults())
          .csrf(AbstractHttpConfigurer::disable)
          .exceptionHandling(
              ex ->
                  ex.authenticationEntryPoint(jwtAuthenticationEntryPoint)
                      .accessDeniedHandler(jwtAccessDeniedHandler))
          .authorizeHttpRequests(
              authorize ->
                  authorize
                      .requestMatchers(CorsUtils::isPreFlightRequest)
                      .permitAll()
                      .requestMatchers(HttpMethod.GET, AUTH_WHITELIST_POLICY)
                      .permitAll()
                      .requestMatchers(HttpMethod.GET, AUTH_WHITELIST_SIGNUP)
                      .permitAll()
                      .requestMatchers(HttpMethod.GET, AUTH_WHITELIST_CLUB)
                      .permitAll()
                      .requestMatchers(HttpMethod.GET, AUTH_WHITELIST_NORMAL_BOARD)
                      .permitAll()
                      .requestMatchers(HttpMethod.GET, AUTH_WHITELIST_PROJECT_BOARD)
                      .permitAll()
                      .requestMatchers(HttpMethod.GET, AUTH_WHITELIST_CONTEST_BOARD)
                      .permitAll()
                      .requestMatchers(AUTH_WHITELIST_SWAGGER)
                      .permitAll()
                      .requestMatchers(AUTH_WHITELIST_STATIC)
                      .permitAll()
                      .requestMatchers(AUTH_WHITELIST_PATH)
                      .permitAll()
                      .requestMatchers("/myInfo/requests")
                      .hasAnyRole(CHIEF.toString(), VICE_CHIEF.toString())
                      .requestMatchers("/myInfo/request/**")
                      .hasAnyRole(CHIEF.toString(), VICE_CHIEF.toString())
                      // 회원 관리
                      .requestMatchers("/members/executive")
                      .hasRole(ANONYMOUS.toString())
                      .requestMatchers("/members/hof")
                      .hasRole(DEACTIVATED.toString())
                      .requestMatchers("/members/approved/role")
                      .hasRole(SECRETARY.toString())
                      .requestMatchers("/members/approved/type")
                      .hasAnyRole(CHIEF.toString(), VICE_CHIEF.toString())
                      .requestMatchers("/members/**", "/member/**")
                      .hasAnyRole(SECRETARY.toString(), EXECUTIVES.toString())
                      // 회계내역
                      .requestMatchers(
                          "/budget/history/**",
                          "/budget/histories",
                          "/budget/application/**",
                          "/budget/applications")
                      .hasRole(DEACTIVATED.toString())
                      // 강의
                      .requestMatchers(
                          "/lecture/*/status",
                          "/lecture/*/students/status",
                          "/lecture/*/student/*/status")
                      .hasRole(EXECUTIVES.toString())
                      .requestMatchers("/lecture/**")
                      .hasRole(DEACTIVATED.toString())
                      // 회원가입 일정 수정
                      .requestMatchers(HttpMethod.PUT, "/signUp/schedule")
                      .hasAnyRole(CHIEF.toString(), VICE_CHIEF.toString())
                      // 회원가입은 ANONYMOUS 권한은 명시적으로 부여받은 상태에서만 가능
                      .requestMatchers("/signUp/check")
                      .hasRole(ANONYMOUS.toString())
                      .requestMatchers("/signUp/**")
                      .hasRole(SIGNING_UP.toString())
                      // 동아리 연혁 수정
                      .requestMatchers("/club/history/**")
                      .hasRole(EXECUTIVES.toString())
                      // 정책 수정
                      .requestMatchers(HttpMethod.PUT, "/policy/**")
                      .hasAnyRole(CHIEF.toString(), VICE_CHIEF.toString())
                      // 장학회 연혁 수정
                      .requestMatchers("/scholarship/history/**")
                      .hasRole(SECRETARY.toString())
                      // 그 외
                      .anyRequest()
                      .hasRole(ANONYMOUS.toString()));

      http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
      return http.build();
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
      return hierarchy.getHierarchy();
    }
  }
}
