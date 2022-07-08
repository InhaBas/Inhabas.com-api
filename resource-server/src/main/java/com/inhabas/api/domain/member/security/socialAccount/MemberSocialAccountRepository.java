package com.inhabas.api.domain.member.security.socialAccount;

import com.inhabas.api.auth.domain.oauth2.OAuth2Provider;
import com.inhabas.api.domain.member.domain.valueObject.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberSocialAccountRepository
        extends JpaRepository<MemberSocialAccount, Integer>, MemberSocialAccountRepositoryCustom {

    /**
     * 레거시 호환성을 위해 존재. "회원가입 되어있지만 uid가 존재하지 않는 경우"에만 사용할 것.
     * @param email 로그인 이메일
     * @param provider 소셜로그인 기관
     * @return 학번
     * @see <a href="https://github.com/InhaBas/Inhabas.com/issues/102">Inhabas/issues/102</a>
     */
    Optional<MemberSocialAccount> findMemberSocialAccountByEmailAndProvider(Email email, OAuth2Provider provider);
}
