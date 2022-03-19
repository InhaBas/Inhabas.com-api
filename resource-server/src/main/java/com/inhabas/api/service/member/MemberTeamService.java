package com.inhabas.api.service.member;

public interface MemberTeamService {

    void addMemberToTeam(Integer memberId, Integer TeamId);

    void deleteMemberFromTeam(Integer memberId, Integer TeamId);
}
