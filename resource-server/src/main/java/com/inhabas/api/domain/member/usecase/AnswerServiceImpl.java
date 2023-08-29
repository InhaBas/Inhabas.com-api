package com.inhabas.api.domain.member.usecase;

import com.inhabas.api.domain.member.domain.entity.Member;
import com.inhabas.api.domain.member.domain.valueObject.MemberId;
import com.inhabas.api.domain.member.repository.MemberRepository;
import com.inhabas.api.domain.member.domain.entity.Answer;
import com.inhabas.api.domain.member.repository.AnswerRepository;
import com.inhabas.api.domain.member.dto.AnswerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository answerRepository;
    private final MemberRepository memberRepository;

    public void saveAnswers(List<AnswerDto> submittedAnswers, MemberId memberId) {

        Member currentMember = memberRepository.getById(memberId);

        List<Answer> answers = submittedAnswers.stream()
                .map(a -> new Answer(currentMember, a.getQuestionNo(), a.getAnswer()))
                .collect(Collectors.toList());

        answerRepository.saveAll(answers);
    }

    public List<AnswerDto> getAnswers(MemberId memberId) {

        return answerRepository.findByMember_Id(memberId).stream()
                .map(a-> new AnswerDto(a.getQuestNo(), a.getCONTENT()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean existAnswersWrittenBy(MemberId memberId) {
        return answerRepository.existsByMember_id(memberId);
    }
}
