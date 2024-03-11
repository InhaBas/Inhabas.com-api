package com.inhabas.api.config;

import static com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role.*;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
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

  private static final String[] AUTH_WHITELIST_PROJECT_BOARD = {"/proejct/count"};

  private static final String[] AUTH_WHITELIST_CONTEST_BOARD = {"/contest/count"};

  @Order(1)
  @EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
  @EnableWebSecurity
  @RequiredArgsConstructor
  @Profile({"local", "dev", "default_mvc_test", "production", "integration_test"})
  public static class ApiSecurityForDev extends WebSecurityConfigurerAdapter {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final Hierarchical hierarchy;
    private final JwtTokenUtil jwtTokenUtil;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final AuthBeansConfig authBeansConfig;

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
      auth.authenticationProvider(jwtAuthenticationProvider);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
      web.ignoring()
          .antMatchers(HttpMethod.GET, AUTH_WHITELIST_POLICY)
          .antMatchers(HttpMethod.GET, AUTH_WHITELIST_SIGNUP)
          .antMatchers(HttpMethod.GET, AUTH_WHITELIST_CLUB)
          .antMatchers(HttpMethod.GET, AUTH_WHITELIST_NORMAL_BOARD)
          .antMatchers(HttpMethod.GET, AUTH_WHITELIST_PROJECT_BOARD)
          .antMatchers(HttpMethod.GET, AUTH_WHITELIST_CONTEST_BOARD)
          .antMatchers(AUTH_WHITELIST_SWAGGER)
          .antMatchers(AUTH_WHITELIST_STATIC)
          .antMatchers(AUTH_WHITELIST_PATH);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http
          // OAuth 관련 경로 제외 모든 경로
          .requestMatchers()
          .antMatchers("/**")
          .and()
          .anonymous()
          .authorities("ROLE_" + ANONYMOUS)
          .and()
          // HTTP 기본 인증 비활성화
          .httpBasic()
          .disable()
          // 세션 생성 금지
          .sessionManagement()
          .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
          .and()
          // API 이므로 CORS 활성화, CSRF 비활성화
          .cors()
          .and()
          .csrf()
          .disable()

          // 인증 예외 처리시 jwtAuthenticationEntryPoint 사용
          .exceptionHandling()
          .authenticationEntryPoint(jwtAuthenticationEntryPoint)
          .accessDeniedHandler(jwtAccessDeniedHandler)
          .and()
          .authorizeRequests()
          // 권한 부여 표현식
          .expressionHandler(expressionHandler())
          // Preflight 방식
          .requestMatchers(CorsUtils::isPreFlightRequest)
          .permitAll()
          .antMatchers("/myInfo/requests")
          .hasAnyRole(CHIEF.toString(), VICE_CHIEF.toString())
          .antMatchers("/myInfo/request/**")
          .hasAnyRole(CHIEF.toString(), VICE_CHIEF.toString())

          // 회원 관리
          .antMatchers("/members/executive")
          .hasRole(ANONYMOUS.toString())
          .antMatchers("/members/hof")
          .hasRole(DEACTIVATED.toString())
          .antMatchers("/members/approved/role")
          .hasRole(SECRETARY.toString())
          .antMatchers("/members/approved/type")
          .hasAnyRole(CHIEF.toString(), VICE_CHIEF.toString())
          .antMatchers("/members/**", "/member/**")
          .hasAnyRole(SECRETARY.toString(), EXECUTIVES.toString())
          // 회계내역
          .antMatchers(
              "/budget/history/**",
              "/budget/histories",
              "/budget/application/**",
              "/budget/applications")
          .hasRole(DEACTIVATED.toString())
          // 강의
          .antMatchers("/lecture/**/status")
          .hasRole(EXECUTIVES.toString())
          .antMatchers("/lecture/**")
          .hasRole(DEACTIVATED.toString())

          // 회원가입 일정 수정
          .antMatchers(HttpMethod.PUT, "/signUp/schedule")
          .hasAnyRole(CHIEF.toString(), VICE_CHIEF.toString())

          // 회원가입은 ANONYMOUS 권한은 명시적으로 부여받은 상태에서만 가능
          .antMatchers("/signUp/check")
          .hasRole(ANONYMOUS.toString())
          .antMatchers("/signUp/**")
          .hasRole(SIGNING_UP.toString())

          // 동아리 연혁 수정
          .antMatchers("/club/history/**")
          .hasRole(EXECUTIVES.toString())

          // 정책 수정
          .antMatchers(HttpMethod.PUT, "/policy/**")
          .hasAnyRole(CHIEF.toString(), VICE_CHIEF.toString())

          // 그 외
          .anyRequest()
          .hasRole(ANONYMOUS.toString());

      http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public SecurityExpressionHandler<FilterInvocation> expressionHandler() {
      DefaultWebSecurityExpressionHandler webSecurityExpressionHandler =
          new DefaultWebSecurityExpressionHandler();
      webSecurityExpressionHandler.setRoleHierarchy(hierarchy.getHierarchy());
      return webSecurityExpressionHandler;
    }

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

      filter.setAuthenticationManager(super.authenticationManager());
      return filter;
    }
  }
}
