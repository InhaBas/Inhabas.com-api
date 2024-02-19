package com.inhabas.api.auth.domain.token.securityFilter;

import org.springframework.security.core.Authentication;

/**
 * 인증 과정에서 얻은 사용자 정보를 바탕으로, 기존에 db에 저장되어 있는 사용자 정보를 얻어오도록 한다. spring security 에서 principal 에 해당하는
 * 객체를 반환하도록 한다. 구현은 이 패키지를 사용하는 하위 종속 모듈에 맡긴다. 따로 이 인터페이스를 구현하지 않으면 {@code
 * DefaultUserPrincipalService} 가 기본으로 설정되는데 단순히 null 을 반환한다.
 */
public interface UserPrincipalService {

  <S extends Authentication> Object loadUserPrincipal(S authentication);
}
