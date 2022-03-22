package com.inhabas.api.security.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * for Type-Safe Property Binding.
 * @see <a href="https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#features.external-config.typesafe-configuration-properties">typesafe-configuration-properties</a>
 */
@Getter @Setter
@Component
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ConfigurationProperties("authenticate")
public class AuthenticateEndPointUrlProperties {
    private String oauth2SuccessHandleUrl;
    private String oauth2FailureHandleUrl;
}
