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
public class OriginProviderForDevelopment implements HttpOriginProvider {

    @Override
    public StringBuffer getOrigin(HttpServletRequest request) {

        StringBuffer url = new StringBuffer();
        String scheme = request.getScheme();
        int port = request.getRemotePort();
        if (port < 0) {
            // Work around java.net.URL bug
            port = 80;
        }

        url.append(scheme);
        url.append("://");
        url.append(request.getRemoteHost());
        if ((scheme.equals("http") && (port != 80))
                || (scheme.equals("https") && (port != 443))) {
            url.append(':');
            url.append(port);
        }

        return url;
    }
}
