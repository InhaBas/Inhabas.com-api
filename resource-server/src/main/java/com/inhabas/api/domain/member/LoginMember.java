package com.inhabas.api.domain.member;

import com.inhabas.api.domain.member.type.wrapper.Role;
import com.inhabas.api.security.utils.argumentResolver.ResolvedAuthenticationResult;

import java.util.List;
import java.util.stream.Collectors;

public class LoginMember extends ResolvedAuthenticationResult {

    public Role getRole() {
        return Role.valueOf(this.role);
    }

    public List<Team> getTeamList() {
        return this.teams.stream().map(Team::new)
                .collect(Collectors.toList());
    }

    public boolean isJoined() {
        Role role = Role.valueOf(this.role);
        return role != Role.ANONYMOUS;
    }
}
