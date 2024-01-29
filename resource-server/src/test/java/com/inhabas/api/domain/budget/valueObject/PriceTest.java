package com.inhabas.api.domain.budget.valueObject;

import static org.assertj.core.api.Assertions.assertThat;

import com.inhabas.api.domain.budget.domain.valueObject.Price;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PriceTest {

  @DisplayName("가격을 생성한다.")
  @Test
  public void normalPriceTest() {
    // given
    Integer price = 10000;

    // when
    Price priceObj = new Price(price);

    // then
    assertThat(priceObj.getValue()).isEqualTo(10000);
  }

  @DisplayName("가격은 0 이 될 수 있다.")
  @Test
  public void zeroPriceTest() {
    // given
    Integer price = 0;

    // when
    Price priceObj = new Price(price);

    // then
    assertThat(priceObj.getValue()).isEqualTo(0);
  }

  @DisplayName("음수 가격은 생성될 수 없다.")
  @Test
  public void minusPriceCannotBeProducedTest() {
    // given
    Integer minusPrice = -10000;

    // when
    Assertions.assertThrows(IllegalArgumentException.class, () -> new Price(minusPrice));
  }
}
