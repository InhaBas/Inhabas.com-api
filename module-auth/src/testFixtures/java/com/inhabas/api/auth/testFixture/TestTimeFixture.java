package com.inhabas.api.auth.testFixture;

import java.time.Instant;
import java.time.LocalDateTime;

/** 테스트 데이터 생성용 고정 시각. 실행 시점과 무관하게 결정적인 값을 보장한다. (실제 시간의 흐름을 검증해야 하는 테스트에는 사용하지 말 것) */
public class TestTimeFixture {

  public static final LocalDateTime FIXED_TIME = LocalDateTime.of(2024, 1, 15, 12, 0, 0);

  public static final Instant FIXED_INSTANT = Instant.parse("2024-01-15T12:00:00Z");
}
