package com.inhabas.api.auth.domain.token.securityFilter;

import org.springframework.security.core.Authentication;

public interface UserPrincipalService {

     <S extends Authentication> Object loadUserPrincipal(S authentication);
}
