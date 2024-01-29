package com.inhabas.api.domain.policy.respository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inhabas.api.domain.policy.domain.PolicyTerm;

public interface PolicyTermRepository extends JpaRepository<PolicyTerm, Long> {}
