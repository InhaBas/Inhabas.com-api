package com.inhabas.api.security;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/* Type-Safe Property Binding
* https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#features.external-config.typesafe-configuration-properties
* */

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ConfigurationProperties("login")
public class LoginUrlProperty {
    private String successUrl;
    private String failureUrl;
}
