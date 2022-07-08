package com.inhabas.api.domain.majorInfo.repository;

import com.inhabas.api.domain.majorInfo.domain.MajorInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MajorInfoRepository extends JpaRepository<MajorInfo, Integer> {
}
