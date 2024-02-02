package com.inhabas.api.domain.signUp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inhabas.api.domain.signUp.domain.entity.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

  List<Answer> findByMember_Id(Long memberId);

  void deleteByMember_IdIn(List<Long> memberIdList);
}
