package com.inhabas.api.service.questionnaire;

import com.inhabas.api.domain.member.Member;
import com.inhabas.api.domain.member.MemberRepository;
import com.inhabas.api.domain.questionaire.Answer;
import com.inhabas.api.domain.questionaire.AnswerRepository;
import com.inhabas.api.dto.signUp.AnswerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository answerRepository;
    private final MemberRepository memberRepository;

    public void saveAnswers(List<AnswerDto> submittedAnswers, Integer memberId) {

        Member currentMember = memberRepository.getById(memberId);

        List<Answer> answers = submittedAnswers.stream()
                .map(a -> new Answer(currentMember, a.getQuestionNo(), a.getAnswer()))
                .collect(Collectors.toList());

        answerRepository.saveAll(answers);
    }

    public List<AnswerDto> getAnswers(Integer memberId) {

        return answerRepository.findByMember_Id(memberId).stream()
                .map(a-> new AnswerDto(a.getQuestionNo(), a.getAnswer()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean existAnswersWrittenBy(Integer memberId) {
        return answerRepository.existsByMember_id(memberId);
    }
}
