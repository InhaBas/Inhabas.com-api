package com.inhabas.api.auth.domain.oauth2.majorInfo.repository;

import com.inhabas.api.auth.domain.oauth2.majorInfo.domain.MajorInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MajorInfoRepository extends JpaRepository<MajorInfo, Integer> {}
