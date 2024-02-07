package com.inhabas.api.auth.domain.oauth2.member.service;

import java.util.Map;

public interface SMTPService {

  void sendRejectMail(String subject, Map<String, Object> variables, String... to);

  void sendPassMail(String subject, Map<String, Object> variables, String... to);
}
