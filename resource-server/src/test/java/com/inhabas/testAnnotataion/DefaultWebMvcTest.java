package com.inhabas.testAnnotataion;

import com.inhabas.api.auth.domain.oauth2.member.security.DefaultRoleHierarchy;
import com.inhabas.testConfig.TestConfigurationForSecurity;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.ActiveProfiles;

/**
 * - ActiveProfiles("test") 설정이유 <br>
 *  : WebMvcTest 진행 시 cloud config 적용되어 외부 설정 파일을 읽어들이려고 시도함.
 * bootstrap.yml 에 test 환경을 따로 명시하여, 테스트 진행 시에는 바로 테스트를 진행하도록 설정.
 * 또 SecurityFilterChain 을 활성화하게 되면, web 관련 컴포넌트 뿐 아니라 연관된 의존성을 모두 불러와야 하므로,
 * test 환경을 명시하여 security 관련 설정을 읽어오지 못하게 하는 역할. (default FilterChain이 사용됨.)<br><br>
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ActiveProfiles("test") // for disable cloud config & security filter chain
@WebMvcTest(excludeAutoConfiguration = {OAuth2ClientAutoConfiguration.class}) // disable autoload OAuth2-Client-Components from test properties
@Import({DefaultRoleHierarchy.class, TestConfigurationForSecurity.class})
public @interface DefaultWebMvcTest {
    @AliasFor(annotation = WebMvcTest.class, attribute = "value")
    Class<?>[] value() default {};

    @AliasFor(annotation = WebMvcTest.class, attribute = "controllers")
    Class<?>[] controllers() default {};
}
