package com.inhabas.api.domain.signUp.repository;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.repository.MemberRepository;
import com.inhabas.api.domain.member.domain.entity.MemberTest;
import com.inhabas.api.domain.questionnaire.domain.Questionnaire;
import com.inhabas.api.domain.questionnaire.repository.QuestionnaireRepository;
import com.inhabas.api.domain.signUp.domain.entity.Answer;
import com.inhabas.testAnnotataion.DefaultDataJpaTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DefaultDataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private QuestionnaireRepository questionnaireRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TestEntityManager em;


    @AfterEach
    void tearDown() {
        answerRepository.deleteAll();
        questionnaireRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("member_id로 작성한 답변을 가져올 수 있다.")
    void findByMember_Id() {
        //given
        Member member = MemberTest.signingUpMember1();
        memberRepository.save(member);
        Questionnaire questionnaire = new Questionnaire(1L, "hello");
        questionnaireRepository.save(questionnaire);
        String content = "Ok... bye";
        Answer answer = new Answer(member, questionnaire, content);

        //when
        answerRepository.save(answer);
        List<Answer> answers = answerRepository.findByMember_Id(1L);

        //then
        assertThat(answers).contains(answer);

    }
}