package com.inhabas.api.security.domain.wrapper;

import com.inhabas.api.security.domain.socialAccount.type.Provider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class ProviderTest {

    @DisplayName("Provider 길이는 30자 초과하면 안된다.")
    @Test
    public void ProviderLengthMustNotBeGreaterThan30() {
        assertThat(
            Arrays.stream(Provider.values())
                    .filter(provider -> provider.toString().length() > 30)
                    .findAny()).isEmpty();
    }

    @DisplayName("Provider 값이 blank 값이면 안된다.")
    @Test
    public void ProviderValueMustNotBeBlank() {
        assertThat(
                Arrays.stream(Provider.values())
                        .filter(provider -> provider.toString().isBlank())
                        .findAny()).isEmpty();
    }
}
