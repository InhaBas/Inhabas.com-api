package com.inhabas.api.domain.member.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyUtils;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DefaultRoleHierarchy implements Hierarchical {

    /* 기존 권한에 ROLE PREFIX 추가해야함. */
    private static final String ADMIN = "ROLE_ADMIN";
    private static final String CHIEF = "ROLE_CHIEF";
    private static final String EXECUTIVES = "ROLE_EXECUTIVES";
    private static final String BASIC_MEMBER = "ROLE_BASIC_MEMBER";
    private static final String DEACTIVATED_MEMBER = "ROLE_DEACTIVATED_MEMBER";
    private static final String NOT_APPROVED_MEMBER = "ROLE_NOT_APPROVED_MEMBER";


    @Override
    public RoleHierarchy getHierarchy() {

        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();

        Map<String, List<String>> roleHierarchyMap = new HashMap<>() {{
            put(
                    ADMIN,
                    Arrays.asList(CHIEF, EXECUTIVES, BASIC_MEMBER, DEACTIVATED_MEMBER, NOT_APPROVED_MEMBER));
            put(
                    CHIEF,
                    Arrays.asList(EXECUTIVES, BASIC_MEMBER, DEACTIVATED_MEMBER, NOT_APPROVED_MEMBER));
            put(
                    EXECUTIVES,
                    Arrays.asList(BASIC_MEMBER, DEACTIVATED_MEMBER, NOT_APPROVED_MEMBER));
            put(
                    BASIC_MEMBER,
                    Arrays.asList(DEACTIVATED_MEMBER, NOT_APPROVED_MEMBER));
            put(
                    DEACTIVATED_MEMBER,
                    List.of(NOT_APPROVED_MEMBER));
        }};

        String roles = RoleHierarchyUtils.roleHierarchyFromMap(roleHierarchyMap);
        roleHierarchy.setHierarchy(roles);

        return roleHierarchy;
    }
}
