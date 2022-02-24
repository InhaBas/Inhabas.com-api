package com.inhabas.api.service.questionnaire;

import com.inhabas.api.domain.questionaire.QuestionnaireRepository;
import com.inhabas.api.dto.signUp.QuestionnaireDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionnaireServiceImpl implements QuestionnaireService {

    private final QuestionnaireRepository questionnaireRepository;

    @Transactional(readOnly = true)
    public List<QuestionnaireDto> getQuestionnaire() {

        return questionnaireRepository.findAll().stream()
                .map(q->new QuestionnaireDto(q.getNo(), q.getItem()))
                .collect(Collectors.toList());
    }
}
