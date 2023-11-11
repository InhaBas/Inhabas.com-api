package com.inhabas.api.domain.member.repository;

import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.StudentId;
import com.inhabas.api.domain.member.domain.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {

    List<Answer> findByMember_id(StudentId studentId);

    boolean existsByMember_id(StudentId studentId);
}
