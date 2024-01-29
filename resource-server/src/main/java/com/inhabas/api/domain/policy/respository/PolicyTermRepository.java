package com.inhabas.api.domain.policy.respository;

import com.inhabas.api.domain.policy.domain.PolicyTerm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PolicyTermRepository extends JpaRepository<PolicyTerm, Long> {}
