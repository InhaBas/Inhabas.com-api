package com.inhabas.api.auth.domain.oauth2.member.security;

import java.util.*;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyUtils;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DefaultRoleHierarchy implements Hierarchical {

  /* 기존 권한에 ROLE PREFIX 추가해야함. */
  private static final String ADMIN = "ROLE_ADMIN";
  private static final String CHIEF = "ROLE_CHIEF";
  private static final String VICE_CHIEF = "ROLE_VICE_CHIEF";
  private static final String EXECUTIVES = "ROLE_EXECUTIVES";
  private static final String SECRETARY = "ROLE_SECRETARY";
  private static final String BASIC = "ROLE_BASIC";
  private static final String DEACTIVATED = "ROLE_DEACTIVATED";
  private static final String NOT_APPROVED = "ROLE_NOT_APPROVED";
  private static final String SIGNING_UP = "ROLE_SIGNING_UP";
  private static final String ANONYMOUS = "ROLE_ANONYMOUS";

  @Override
  public RoleHierarchy getHierarchy() {

    RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();

    Map<String, List<String>> roleHierarchyMap =
        new HashMap<>() {
          {
            put(
                ADMIN,
                Arrays.asList(
                    CHIEF,
                    VICE_CHIEF,
                    EXECUTIVES,
                    SECRETARY,
                    BASIC,
                    DEACTIVATED,
                    NOT_APPROVED,
                    ANONYMOUS));
            put(
                CHIEF,
                Arrays.asList(EXECUTIVES, SECRETARY, BASIC, DEACTIVATED, NOT_APPROVED, ANONYMOUS));
            put(
                VICE_CHIEF,
                Arrays.asList(EXECUTIVES, SECRETARY, BASIC, DEACTIVATED, NOT_APPROVED, ANONYMOUS));
            put(EXECUTIVES, Arrays.asList(BASIC, DEACTIVATED, NOT_APPROVED, ANONYMOUS));
            put(SECRETARY, Arrays.asList(BASIC, DEACTIVATED, NOT_APPROVED, ANONYMOUS));
            put(BASIC, Arrays.asList(DEACTIVATED, NOT_APPROVED, ANONYMOUS));
            put(DEACTIVATED, Arrays.asList(NOT_APPROVED, ANONYMOUS));
            put(NOT_APPROVED, Arrays.asList(ANONYMOUS));
            put(SIGNING_UP, Arrays.asList(ANONYMOUS));
          }
        };

    String roles = RoleHierarchyUtils.roleHierarchyFromMap(roleHierarchyMap);
    roleHierarchy.setHierarchy(roles);

    return roleHierarchy;
  }
}
