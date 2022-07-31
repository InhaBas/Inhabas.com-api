package com.inhabas.api.auth.domain.oauth2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class OAuth2ProviderTest {

    @DisplayName("Provider 길이는 30자 초과하면 안된다.")
    @Test
    public void ProviderLengthMustNotBeGreaterThan30() {
        assertThat(
            Arrays.stream(OAuth2Provider.values())
                    .filter(provider -> provider.toString().length() > 30)
                    .findAny()).isEmpty();
    }

    @DisplayName("Provider 값이 blank 값이면 안된다.")
    @Test
    public void ProviderValueMustNotBeBlank() {
        assertThat(
                Arrays.stream(OAuth2Provider.values())
                        .filter(provider -> provider.toString().isBlank())
                        .findAny()).isEmpty();
    }
}
