package com.inhabas.api.auth.domain.oauth2.member.repository;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.UpdateNameRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UpdateNameRequestRepository extends JpaRepository<UpdateNameRequest, Long> {
}
