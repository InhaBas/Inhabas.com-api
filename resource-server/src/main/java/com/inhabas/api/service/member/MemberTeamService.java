package com.inhabas.api.service.member;

import com.inhabas.api.domain.member.MemberId;

public interface MemberTeamService {

    void addMemberToTeam(MemberId memberId, Integer TeamId);

    void deleteMemberFromTeam(MemberId memberId, Integer TeamId);
}
