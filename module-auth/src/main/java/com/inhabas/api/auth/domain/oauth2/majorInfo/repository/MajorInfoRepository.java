package com.inhabas.api.auth.domain.oauth2.majorInfo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inhabas.api.auth.domain.oauth2.majorInfo.domain.MajorInfo;

public interface MajorInfoRepository extends JpaRepository<MajorInfo, Integer> {}
