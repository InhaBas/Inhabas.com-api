package com.inhabas.api.service.login;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * This is for local-server and dev-server.
 * Especially dev-server needs to allow requests from all origins
 * because front-end developers use for api-dev-server on their localhost.
 */
@Component
@Profile("!production")
public class HttpOriginProviderForDevelopment implements HttpOriginProvider {

    @Override
    public StringBuffer getOrigin(HttpServletRequest request) {

        return new StringBuffer(request.getHeader("Referer"));  // 프론트엔드 로컬 개발환경으로 리다이렉트
    }
}
