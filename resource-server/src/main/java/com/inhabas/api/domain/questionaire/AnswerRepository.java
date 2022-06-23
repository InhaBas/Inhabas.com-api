package com.inhabas.api.domain.questionaire;

import com.inhabas.api.domain.member.MemberId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {

    List<Answer> findByMember_Id(MemberId memberId);

    boolean existsByMember_id(MemberId memberId);
}
