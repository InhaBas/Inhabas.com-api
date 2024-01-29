package com.inhabas.api.domain.signUp.repository;

import com.inhabas.api.domain.signUp.domain.entity.Answer;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

  List<Answer> findByMember_Id(Long memberId);
}
