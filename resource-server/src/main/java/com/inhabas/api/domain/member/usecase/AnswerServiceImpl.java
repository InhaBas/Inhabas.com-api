package com.inhabas.api.domain.member.usecase;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.StudentId;
import com.inhabas.api.auth.domain.oauth2.member.repository.MemberRepository;
import com.inhabas.api.domain.member.domain.entity.Answer;
import com.inhabas.api.domain.member.dto.AnswerDto;
import com.inhabas.api.domain.member.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository answerRepository;
    private final MemberRepository memberRepository;

    public void saveAnswers(List<AnswerDto> submittedAnswers, StudentId studentId) {

        Member currentMember = memberRepository.getByStudentId(studentId);

        List<Answer> answers = submittedAnswers.stream()
                .map(a -> new Answer(currentMember, a.getQuestionNo(), a.getContent()))
                .collect(Collectors.toList());

        answerRepository.saveAll(answers);
    }

    public List<AnswerDto> getAnswers(Long memberId) {

        return answerRepository.findByMember_Id(memberId).stream()
                .map(a-> new AnswerDto(a.getQuestionNo(), a.getContent()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean existAnswersWrittenBy(StudentId studentId) {
        return answerRepository.existsByMember_id(studentId);
    }
}
