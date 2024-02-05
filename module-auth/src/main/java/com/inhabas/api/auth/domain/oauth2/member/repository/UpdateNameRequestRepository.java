package com.inhabas.api.auth.domain.oauth2.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.UpdateNameRequest;

public interface UpdateNameRequestRepository extends JpaRepository<UpdateNameRequest, Long>, UpdateNameRequestRepositoryCustom {}
