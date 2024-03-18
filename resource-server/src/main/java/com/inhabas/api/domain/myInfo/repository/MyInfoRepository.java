package com.inhabas.api.domain.myInfo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inhabas.api.domain.normalBoard.domain.NormalBoard;

public interface MyInfoRepository
    extends JpaRepository<NormalBoard, Long>, MyInfoRepositoryCustom {}
