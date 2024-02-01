package com.inhabas.api.auth.domain.oauth2.member.service;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class AmazonSMTPService implements SMTPService {

    private final AmazonSimpleEmailService amazonSimpleEmailService;
    private final TemplateEngine htmlTemplateEngine;
    @Value("${cloud.aws.ses.from}")
    private String from;

    private static final String SIGNUP_FAIL_HTML = "signUpFail";
    private static final String SIGNUP_SUCCESS_HTML = "signUpSuccess";


    public AmazonSMTPService(AmazonSimpleEmailService amazonSimpleEmailService, TemplateEngine htmlTemplateEngine) {
        this.amazonSimpleEmailService = amazonSimpleEmailService;
        this.htmlTemplateEngine = htmlTemplateEngine;
    }

    @Override
    public void sendRejectMail(String subject, Map<String, Object> variables, String... to) {

        String content = htmlTemplateEngine.process(SIGNUP_FAIL_HTML, createContext(variables));
        SendEmailRequest sendEmailRequest = createSendEmailRequest(subject, content, to);
        amazonSimpleEmailService.sendEmail(sendEmailRequest);

    }

    @Override
    public void sendPassMail(String subject, Map<String, Object> variables, String... to) {

        String content = htmlTemplateEngine.process(SIGNUP_SUCCESS_HTML, createContext(variables));
        SendEmailRequest sendEmailRequest = createSendEmailRequest(subject, content, to);
        amazonSimpleEmailService.sendEmail(sendEmailRequest);

    }

    private Context createContext(Map<String, Object> variables) {
        Context context = new Context();
        context.setVariables(variables);

        return context;
    }

    private SendEmailRequest createSendEmailRequest(String subject, String content, String... to) {

        return new SendEmailRequest()
                .withDestination(new Destination().withToAddresses(to))
                .withSource(from)
                .withMessage(new Message()
                        .withSubject(new Content().withCharset(StandardCharsets.UTF_8.name()).withData(subject))
                        .withBody(new Body().withHtml(new Content().withCharset(StandardCharsets.UTF_8.name()).withData(content)))
                );

    }

}
