package com.inhabas.api.auth.domain.oauth2.member.security;

import org.springframework.security.access.hierarchicalroles.RoleHierarchy;

/**
 * 로그인된 사용자의 권한을 수직적으로 정의한다. (예로, 회장이면 일반 사용자의 모든 권한을 포함하도록 한다.) 리턴 객체를 Bean 으로 등록해서
 * SecurityExpressionHandler 에 주입해주어야 적용된다. 최종적으로는 주입된 SecurityExpressionHandler 빈을 등록해야한다.
 */
public interface Hierarchical {

  RoleHierarchy getHierarchy();
}
