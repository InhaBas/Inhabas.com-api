package com.inhabas.api.auth.domain.oauth2.member.security;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DefaultRoleHierarchyTest {

  private RoleHierarchy roleHierarchy;

  @BeforeEach
  public void setUp() {
    roleHierarchy = new DefaultRoleHierarchy().getHierarchy();
  }

  private Set<String> reachableAuthoritiesOf(String role) {
    return roleHierarchy
        .getReachableGrantedAuthorities(List.of(new SimpleGrantedAuthority(role)))
        .stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.toSet());
  }

  @DisplayName("ADMIN 은 SIGNING_UP 을 제외한 모든 권한에 도달할 수 있다.")
  @Test
  public void adminReachesAllTest() {
    assertThat(reachableAuthoritiesOf("ROLE_ADMIN"))
        .containsExactlyInAnyOrder(
            "ROLE_ADMIN",
            "ROLE_CHIEF",
            "ROLE_VICE_CHIEF",
            "ROLE_EXECUTIVES",
            "ROLE_SECRETARY",
            "ROLE_BASIC",
            "ROLE_DEACTIVATED",
            "ROLE_NOT_APPROVED",
            "ROLE_ANONYMOUS");
  }

  @DisplayName("CHIEF 는 ADMIN 을 제외한 하위 권한에 모두 도달할 수 있다.")
  @Test
  public void chiefReachesBelowTest() {
    Set<String> reachable = reachableAuthoritiesOf("ROLE_CHIEF");

    assertThat(reachable)
        .containsExactlyInAnyOrder(
            "ROLE_CHIEF",
            "ROLE_VICE_CHIEF",
            "ROLE_EXECUTIVES",
            "ROLE_SECRETARY",
            "ROLE_BASIC",
            "ROLE_DEACTIVATED",
            "ROLE_NOT_APPROVED",
            "ROLE_ANONYMOUS");
    assertThat(reachable).doesNotContain("ROLE_ADMIN");
  }

  @DisplayName("VICE_CHIEF 는 CHIEF 에 도달할 수 없다.")
  @Test
  public void viceChiefCannotReachChiefTest() {
    Set<String> reachable = reachableAuthoritiesOf("ROLE_VICE_CHIEF");

    assertThat(reachable)
        .containsExactlyInAnyOrder(
            "ROLE_VICE_CHIEF",
            "ROLE_EXECUTIVES",
            "ROLE_SECRETARY",
            "ROLE_BASIC",
            "ROLE_DEACTIVATED",
            "ROLE_NOT_APPROVED",
            "ROLE_ANONYMOUS");
  }

  @DisplayName("EXECUTIVES 와 SECRETARY 는 서로 형제 권한이라 서로에게 도달할 수 없다.")
  @Test
  public void executivesAndSecretaryAreSiblingsTest() {
    Set<String> executivesReachable = reachableAuthoritiesOf("ROLE_EXECUTIVES");
    Set<String> secretaryReachable = reachableAuthoritiesOf("ROLE_SECRETARY");

    assertThat(executivesReachable)
        .containsExactlyInAnyOrder(
            "ROLE_EXECUTIVES",
            "ROLE_BASIC",
            "ROLE_DEACTIVATED",
            "ROLE_NOT_APPROVED",
            "ROLE_ANONYMOUS");
    assertThat(executivesReachable).doesNotContain("ROLE_SECRETARY");

    assertThat(secretaryReachable)
        .containsExactlyInAnyOrder(
            "ROLE_SECRETARY",
            "ROLE_BASIC",
            "ROLE_DEACTIVATED",
            "ROLE_NOT_APPROVED",
            "ROLE_ANONYMOUS");
    assertThat(secretaryReachable).doesNotContain("ROLE_EXECUTIVES");
  }

  @DisplayName("BASIC 은 비활동/미승인/익명 권한에만 도달할 수 있다.")
  @Test
  public void basicReachesInactiveRolesTest() {
    assertThat(reachableAuthoritiesOf("ROLE_BASIC"))
        .containsExactlyInAnyOrder(
            "ROLE_BASIC", "ROLE_DEACTIVATED", "ROLE_NOT_APPROVED", "ROLE_ANONYMOUS");
  }

  @DisplayName("SIGNING_UP 과 NOT_APPROVED 는 ANONYMOUS 에만 도달할 수 있다.")
  @Test
  public void signingUpAndNotApprovedReachOnlyAnonymousTest() {
    assertThat(reachableAuthoritiesOf("ROLE_SIGNING_UP"))
        .containsExactlyInAnyOrder("ROLE_SIGNING_UP", "ROLE_ANONYMOUS");
    assertThat(reachableAuthoritiesOf("ROLE_NOT_APPROVED"))
        .containsExactlyInAnyOrder("ROLE_NOT_APPROVED", "ROLE_ANONYMOUS");
  }
}
