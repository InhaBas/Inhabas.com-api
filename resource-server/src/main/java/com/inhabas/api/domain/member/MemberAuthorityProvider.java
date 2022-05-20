package com.inhabas.api.domain.member;

import com.inhabas.api.auth.domain.oauth2.socialAccount.type.UID;
import com.inhabas.api.auth.domain.oauth2.userAuthorityProvider.UserAuthorityProvider;
import com.inhabas.api.auth.domain.oauth2.userInfo.OAuth2UserInfo;
import com.inhabas.api.domain.member.security.socialAccount.MemberSocialAccount;
import com.inhabas.api.domain.member.security.socialAccount.MemberSocialAccountRepository;
import com.inhabas.api.domain.member.type.wrapper.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
@RequiredArgsConstructor
public class MemberAuthorityProvider implements UserAuthorityProvider {

    private final MemberSocialAccountRepository memberSocialAccountRepository;
    private static final String ROLE_PREFIX = "ROLE_";
    private static final String TEAM_PREFIX = "TEAM_";

    @Override
    @Transactional
    public Collection<SimpleGrantedAuthority> determineAuthorities(OAuth2UserInfo oAuth2UserInfo) {

        // provider 와 id 로 조회되는 memberSocialAccount 가 있으면 기존회원임.
        UID uid = new UID(oAuth2UserInfo.getId());
        Optional<MemberSocialAccount> memberSocialAccount =
                memberSocialAccountRepository.findByUidAndProviderWithRoleAndTeam(uid, oAuth2UserInfo.getProvider());

        if (memberSocialAccount.isPresent()) { // 기존회원이면
            Member member = memberSocialAccount.get().getMember();
            Role role = member.getRole();
            Collection<Team> teamList = member.getTeamList();

            return new HashSet<>() {{
                add(new SimpleGrantedAuthority(ROLE_PREFIX + role.toString()));
                teamList.forEach(team -> add(new SimpleGrantedAuthority(TEAM_PREFIX + team.getName())));
            }};

        } else {
            return Collections.singleton(new SimpleGrantedAuthority(ROLE_PREFIX + Role.ANONYMOUS));
        }
    }
}
