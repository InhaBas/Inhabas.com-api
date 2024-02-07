package com.inhabas.api.auth.domain.oauth2.member.repository;

import java.util.List;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.UpdateNameRequest;

public interface UpdateNameRequestRepositoryCustom {

  List<UpdateNameRequest> findAllOrderByStatusAndDateRequested();

  List<UpdateNameRequest> findAllByMemberIdOrderByStatusAndDateRequested(Long memberId);
}
