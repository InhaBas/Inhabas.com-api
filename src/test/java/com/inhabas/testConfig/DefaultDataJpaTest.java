package com.inhabas.testConfig;

import com.inhabas.api.config.JpaConfig;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * - ActiveProfiles("test") 설정이유 <br>
 *  : WebMvcTest 진행 시 cloud config 적용되어 외부 설정 파일을 읽어들이려고 시도함.
 * bootstrap.yml 에 test 환경을 따로 명시하여, 테스트 진행 시에는 바로 테스트를 진행하도록 설정.
 * 또 SecurityFilterChain 을 활성화하게 되면, web 관련 컴포넌트 뿐 아니라 연관된 의존성을 모두 불러와야 하므로,
 * test 환경을 명시하여 security 관련 설정을 읽어오지 못하게 하는 역할. (default FilterChain이 사용됨.)<br><br>
 * - Import(JpaConfig.class) 설정이유 <br>
 *  : QueryDsl 을 사용하기 위해 JPAQueryFactory 를 빈으로 등록하고 주입해야한다.
 *  하지만 JPAQueryFactory 는 DataJpaTest 에 의해 자동 주입되지 않는다.
 *  DataJpaTest 는 spring-boot-start-data 패키지에 포함된 jpa 관련 컴포넌트만 가져온다.
 *  QueryDsl 은 다른 패키지에 속해있다. 따라서 JPAQueryFactory 를 수동으로 가져오기 위한 작업이다.
 * @see DefaultWebMvcTest
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@DataJpaTest
@ActiveProfiles("test")
@Import(JpaConfig.class)
public @interface DefaultDataJpaTest {
}
