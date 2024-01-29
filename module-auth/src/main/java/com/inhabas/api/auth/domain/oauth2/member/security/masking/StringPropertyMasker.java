package com.inhabas.api.auth.domain.oauth2.member.security.masking;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class StringPropertyMasker extends StdSerializer<String> implements ContextualSerializer {
  private MaskingType maskingType;

  protected StringPropertyMasker() {
    super(String.class);
  }

  protected StringPropertyMasker(MaskingType maskingType) {
    super(String.class);
    this.maskingType = maskingType;
  }

  @Override
  public void serialize(String value, JsonGenerator gen, SerializerProvider provider)
      throws IOException {
    gen.writeString(Masking.mask(maskingType, value));
  }

  @Override
  public JsonSerializer<?> createContextual(
      SerializerProvider serializerProvider, BeanProperty property) {
    if (property != null) {
      Masked masked = property.getAnnotation(Masked.class);
      if (masked != null) {
        // 필드에 @Masked 애노테이션이 있는 경우, 애노테이션의 값에 따라 적절한 마스킹 로직을 선택하여 반환합니다.
        return new StringPropertyMasker(masked.type());
      }
    }
    return this;
  }
}
