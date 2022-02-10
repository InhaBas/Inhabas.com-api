package com.inhabas.api.security;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/* Type-Safe Property Binding
* https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#features.external-config.typesafe-configuration-properties
* */

@Getter @Setter
@Component
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ConfigurationProperties("authenticate")
public class AuthenticateEndPointUrlProperties {
    private String oauth2SuccessHandleUrl;
    private String oauth2FailureHandleUrl;
    private String invalidJwtTokenHandleUrl;
    private String reissueAccessTokenUrl;
}
