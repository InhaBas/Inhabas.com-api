package com.inhabas.api.domain.board.domain;

public enum PinOption {
  DISABLED(0),
  TEMPORARY(1),
  PERMANENT(2);

  private final Integer option;

  PinOption(Integer option) {
    this.option = option;
  }

  public Integer getOption() {
    return option;
  }
}
