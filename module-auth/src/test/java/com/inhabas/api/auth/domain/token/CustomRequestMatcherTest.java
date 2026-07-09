package com.inhabas.api.auth.domain.token;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.mock.web.MockHttpServletRequest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CustomRequestMatcherTest {

  @DisplayName("경로와 메서드가 모두 일치하면 false를 반환한다.")
  @Test
  void notMatchesWhenRequestMatches() {
    // given
    CustomRequestMatcher matcher = new CustomRequestMatcher("/menus", "GET");
    MockHttpServletRequest request = new MockHttpServletRequest("GET", "/menus");

    // when then
    assertThat(matcher.matches(request)).isFalse();
  }

  @DisplayName("경로가 일치하지 않으면 true를 반환한다.")
  @Test
  void matchesWhenPathDiffers() {
    // given
    CustomRequestMatcher matcher = new CustomRequestMatcher("/menus", "GET");
    MockHttpServletRequest request = new MockHttpServletRequest("GET", "/members");

    // when then
    assertThat(matcher.matches(request)).isTrue();
  }

  @DisplayName("HTTP 메서드가 일치하지 않으면 true를 반환한다.")
  @Test
  void matchesWhenMethodDiffers() {
    // given
    CustomRequestMatcher matcher = new CustomRequestMatcher("/menus", "GET");
    MockHttpServletRequest request = new MockHttpServletRequest("POST", "/menus");

    // when then
    assertThat(matcher.matches(request)).isTrue();
  }

  @DisplayName("경로 패턴 변수도 매칭에 반영된다.")
  @Test
  void notMatchesWithPathVariablePattern() {
    // given
    CustomRequestMatcher matcher = new CustomRequestMatcher("/menu/{menuId}", "GET");
    MockHttpServletRequest request = new MockHttpServletRequest("GET", "/menu/1");

    // when then
    assertThat(matcher.matches(request)).isFalse();
  }
}
