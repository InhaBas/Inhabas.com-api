package com.inhabas.api.security.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthUserService {

    private final AuthUserRepository authUserRepository;

    @Transactional(readOnly = true)
    public AuthUser loadUser(Integer id) {
        // 나중에 캐쉬 처리해야됨.
        return authUserRepository.findById(id)
                .orElseThrow(AuthUserNotFoundException::new);
    }

    @Transactional
    public void setProfileIdToSocialAccount(Integer authUserId, Integer memberId) {
        AuthUser authUser = authUserRepository.findById(authUserId)
                .orElseThrow(AuthUserNotFoundException::new);

        authUser.setProfileId(memberId);
        authUserRepository.save(authUser);
    }
}
