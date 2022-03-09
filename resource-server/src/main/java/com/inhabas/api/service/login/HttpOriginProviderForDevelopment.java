package com.inhabas.api.service.login;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

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

        String host = request.getHeader("Host");

        if (Objects.equals(host, "dev.inhabas.com")) {
            return new StringBuffer("https://dev.inhabas.com");
        }
        else {
            return new StringBuffer(String.format("http://%s", host));
        }
    }
}
