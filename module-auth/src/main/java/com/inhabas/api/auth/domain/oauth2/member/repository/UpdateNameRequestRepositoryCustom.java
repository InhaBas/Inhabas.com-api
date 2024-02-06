package com.inhabas.api.auth.domain.oauth2.member.repository;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.UpdateNameRequest;

import java.util.List;

public interface UpdateNameRequestRepositoryCustom {

    List<UpdateNameRequest> findAllOrderByStatusAndDateRequested();
    List<UpdateNameRequest> findAllByMemberIdOrderByStatusAndDateRequested(Long memberId);

}
