package com.inhabas.api.domain.member.repository;

import com.inhabas.api.domain.member.domain.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    List<Answer> findByMember_Id(Long memberId);

    boolean existsByMember_Id(Long memberId);
}
