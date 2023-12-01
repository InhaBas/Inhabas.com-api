package com.inhabas.api.domain.member.usecase;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.exception.MemberNotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.repository.MemberRepository;
import com.inhabas.api.domain.member.domain.entity.Answer;
import com.inhabas.api.domain.member.dto.AnswerDto;
import com.inhabas.api.domain.member.repository.AnswerRepository;
import com.inhabas.api.domain.questionaire.domain.Questionnaire;
import com.inhabas.api.domain.questionaire.repository.QuestionnaireRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository answerRepository;
    private final MemberRepository memberRepository;
    private final QuestionnaireRepository questionnaireRepository;


    @Override
    @Transactional
    public void saveAnswers(List<AnswerDto> submittedAnswers, Long memberId) {

        Member currentMember = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);

        // 제출 답안의 질문번호 리스트 questionIds
        List<Long> questionIds = submittedAnswers.stream()
                .map(AnswerDto::getQuestionId)
                .collect(Collectors.toList());

        // 모든 질문 questionnaireList
        List<Questionnaire> questionnaireList = questionnaireRepository.findAll();
        // 모든 질문 번호 questionnaireIdList
        List<Long> questionnaireIdList = questionnaireList.stream()
                .map(Questionnaire::getId)
                .collect(Collectors.toList());

        if (questionnaireRepository.countByIdIn(questionIds) != questionnaireIdList.size()) {
            throw new IllegalArgumentException("Some question IDs are invalid");
        }

        // 기존 답변 가져오기
        Map<Long, Answer> existingAnswers = answerRepository.findByMember_Id(memberId).stream()
                .collect(Collectors.toMap(answer -> answer.getQuestionnaire().getId(), answer -> answer));

        // 새 답변 또는 업데이트된 답변 준비
        List<Answer> answersToUpdate = submittedAnswers.stream()
                .map(dto -> {
                    Questionnaire questionnaire = questionnaireRepository.findById(dto.getQuestionId())
                            .orElseThrow(() -> new EntityNotFoundException("Questionnaire not found with id: " + dto.getQuestionId()));

                    Answer existingAnswer = existingAnswers.get(dto.getQuestionId());
                    if (existingAnswer != null) {
                        existingAnswer.setContent(dto.getContent());
                        return existingAnswer;
                    } else {
                        return new Answer(currentMember, questionnaire, dto.getContent());
                    }
                })
                .collect(Collectors.toList());

        answerRepository.saveAll(answersToUpdate);

    }

    @Override
    public List<AnswerDto> getAnswers(Long memberId) {

        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);

        List<Questionnaire> questionnaires = questionnaireRepository.findAll();

        Map<Long, Answer> answersByQuestionnaire = answerRepository.findByMember_Id(memberId)
                .stream()
                .collect(Collectors.toMap(
                        answer -> answer.getQuestionnaire().getId(),
                        answer -> answer
                ));

        return questionnaires.stream()
                .map(questionnaire -> {
                    Long questionnaireId = questionnaire.getId();
                    Answer answer = answersByQuestionnaire.getOrDefault(questionnaireId, new Answer(member, questionnaire, null));
                    return new AnswerDto(questionnaireId, answer.getContent());
                })
                .collect(Collectors.toList());
    }

}
